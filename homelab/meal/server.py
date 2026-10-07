#!/usr/bin/env python3
"""식단표 기록 저장 API. 표준 라이브러리만 쓴다 (http.server + sqlite3).

식단표 페이지는 기록을 localStorage에 JSON 하나로 저장한다. sync.js가 그 JSON을
"섹션/키" 단위 변경으로 쪼개 보내면, 여기서는 키마다 가장 늦게 바뀐 값만 남긴다.
그래서 두 기기에서 서로 다른 끼니를 고쳐도 둘 다 남는다.

  GET  /meal/api/doc/<문서키>          -> {"key", "rev", "state"}
  POST /meal/api/doc/<문서키>/changes  <- {"changes": [{"path": [섹션, 키] | [키], "value": 값|null, "at": ms}]}

127.0.0.1에만 바인딩한다. 접근 제어는 앞단(Tailscale 전용 Caddy)이 맡는다.
"""
import json
import os
import re
import sqlite3
import threading
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer

DB_PATH = os.environ.get("MEAL_DB", os.path.expanduser("~/meal-data/meal.db"))
HOST, PORT = "127.0.0.1", int(os.environ.get("MEAL_PORT", "8083"))
MAX_BODY = 1 << 20
DOC_RE = re.compile(r"^/meal/api/doc/(meal-[0-9]{4}-[0-9]{2}[A-Za-z0-9_-]{0,40})(/changes)?$")
ROOT = ""  # 최상위 값(예: "v")은 섹션 이름을 비워서 저장한다

lock = threading.Lock()


def connect():
    os.makedirs(os.path.dirname(DB_PATH), exist_ok=True)
    db = sqlite3.connect(DB_PATH, check_same_thread=False)
    db.execute("PRAGMA journal_mode=WAL")
    db.executescript("""
        CREATE TABLE IF NOT EXISTS entries (
            doc     TEXT NOT NULL,
            section TEXT NOT NULL,
            key     TEXT NOT NULL,
            value   TEXT,              -- JSON, NULL이면 지워진 키
            at      INTEGER NOT NULL,  -- 클라이언트가 바꾼 시각 (ms)
            PRIMARY KEY (doc, section, key)
        );
        CREATE TABLE IF NOT EXISTS docs (
            doc TEXT PRIMARY KEY,
            rev INTEGER NOT NULL
        );
    """)
    return db


db = connect()


def load(doc):
    rev = db.execute("SELECT rev FROM docs WHERE doc = ?", (doc,)).fetchone()
    state = {}
    for section, key, value in db.execute(
            "SELECT section, key, value FROM entries WHERE doc = ? AND value IS NOT NULL", (doc,)):
        if section == ROOT:
            state[key] = json.loads(value)
        else:
            state.setdefault(section, {})[key] = json.loads(value)
    return {"key": doc, "rev": rev[0] if rev else 0, "state": state}


def apply(doc, changes):
    applied = 0
    with lock, db:
        for c in changes:
            path, at = c.get("path"), c.get("at")
            if not isinstance(path, list) or not 1 <= len(path) <= 2 or not all(isinstance(p, str) for p in path):
                raise ValueError("path는 [키] 또는 [섹션, 키]")
            if not isinstance(at, int):
                raise ValueError("at은 정수(ms)")
            section, key = (ROOT, path[0]) if len(path) == 1 else (path[0], path[1])
            value = None if c.get("value") is None else json.dumps(c["value"], ensure_ascii=False)
            cur = db.execute(
                """INSERT INTO entries (doc, section, key, value, at) VALUES (?, ?, ?, ?, ?)
                   ON CONFLICT (doc, section, key) DO UPDATE SET value = excluded.value, at = excluded.at
                   WHERE excluded.at >= entries.at""",
                (doc, section, key, value, at))
            applied += cur.rowcount
        db.execute("INSERT INTO docs (doc, rev) VALUES (?, 1) ON CONFLICT (doc) DO UPDATE SET rev = rev + 1", (doc,))
    return applied


class Handler(BaseHTTPRequestHandler):
    server_version = "meal-api"

    def send(self, code, body):
        data = json.dumps(body, ensure_ascii=False).encode()
        self.send_response(code)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.send_header("Cache-Control", "no-store")
        self.send_header("Content-Length", str(len(data)))
        self.end_headers()
        self.wfile.write(data)

    def do_GET(self):
        m = DOC_RE.match(self.path)
        if not m or m.group(2):
            return self.send(404, {"error": "not found"})
        self.send(200, load(m.group(1)))

    def do_POST(self):
        m = DOC_RE.match(self.path)
        if not m or not m.group(2):
            return self.send(404, {"error": "not found"})
        length = int(self.headers.get("Content-Length") or 0)
        if not 0 < length <= MAX_BODY:
            return self.send(413, {"error": "body too large or empty"})
        try:
            changes = json.loads(self.rfile.read(length)).get("changes")
            if not isinstance(changes, list):
                raise ValueError("changes는 배열")
            applied = apply(m.group(1), changes)
        except (ValueError, AttributeError, json.JSONDecodeError) as e:
            return self.send(400, {"error": str(e)})
        doc = load(m.group(1))
        self.send(200, {"rev": doc["rev"], "applied": applied})

    def log_message(self, fmt, *args):
        pass  # 기록 내용이 로그에 남지 않게 한다


if __name__ == "__main__":
    print(f"meal-api: {HOST}:{PORT}, db={DB_PATH}", flush=True)
    ThreadingHTTPServer((HOST, PORT), Handler).serve_forever()
