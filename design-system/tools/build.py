#!/usr/bin/env python3
"""디자인 시스템 빌드 — 시스템 폴더(system.json 이 있는 곳)마다

  tokens.json  → tokens.css(밝은/어두운 모드) · tokens.js(조종 패널용)
  cards/*.md   → lookbook/catalog.js(룩북이 읽는 블록 목록)

를 만든다. 표준 라이브러리만 쓴다. 실행: python3 tools/build.py
"""
import hashlib
import json
import re
from datetime import date
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
HEAD = re.compile(r"^###\s+([A-Z]-\d{2})\s*·\s*(.+?)\s*$")
FIELD = re.compile(r"^- ([^:：]{1,12})[:：]\s?(.*)$")


def token_css(data):
    light, dark = [], []
    for g in data["groups"]:
        light.append(f"  /* {g['name']} */")
        for t in g["tokens"]:
            light.append(f"  --{t['name']}: {t.get('value', t.get('light'))};")
            if "dark" in t:
                dark.append(f"  --{t['name']}: {t['dark']};")
    dark_body = "\n".join(dark)
    return (
        f"/* {data['system']} 토큰 — tools/build.py 가 tokens.json 에서 만든다. 손으로 고치지 않는다 */\n"
        ":root {\n  color-scheme: light;\n" + "\n".join(light) + "\n}\n"
        "[data-mode=\"dark\"] {\n  color-scheme: dark;\n" + dark_body + "\n}\n"
        "@media (prefers-color-scheme: dark) {\n  :root:not([data-mode=\"light\"]) {\n    color-scheme: dark;\n"
        + "\n".join("  " + line for line in dark) + "\n  }\n}\n"
    )


def parse_cards(folder, cats):
    cards = []
    for md in sorted(folder.glob("*.md")):
        card = None
        for line in md.read_text(encoding="utf-8").splitlines():
            h = HEAD.match(line)
            if h:
                card = {"id": h.group(1), "cat": h.group(1)[0], "name": h.group(2), "file": md.name, "fields": []}
                cards.append(card)
                continue
            if card is None:
                continue
            f = FIELD.match(line)
            if f:
                card["fields"].append([f.group(1).strip(), f.group(2).strip()])
            elif line.startswith("  ") and line.strip() and card["fields"]:
                card["fields"][-1][1] += "\n" + line.strip()
    order = list(cats)
    cards.sort(key=lambda c: (order.index(c["cat"]) if c["cat"] in order else 99, c["id"]))
    return cards


def parse_dictionary(path):
    """블록이 없는 표준 부품 — ### 이름 + - 키: 값 줄. 룩북이 찾기 · 담기에 쓴다"""
    if not path.exists():
        return []
    items, cur = [], None
    for line in path.read_text(encoding="utf-8").splitlines():
        h = re.match(r"^###\s+(.+?)\s*$", line)
        if h:
            cur = {"name": h.group(1), "fields": []}
            items.append(cur)
            continue
        f = FIELD.match(line)
        if cur and f:
            cur["fields"].append([f.group(1).strip(), f.group(2).strip()])
    return items


def example_ids(path):
    if not path.exists():
        return []
    return re.findall(r"\bex\('([A-Z]-\d{2})'", path.read_text(encoding="utf-8"))


def main():
    systems = []
    for sj in sorted(ROOT.glob("*/system.json")):
        folder = sj.parent
        meta = json.loads(sj.read_text(encoding="utf-8"))
        tj = folder / "tokens.json"
        swatches = []
        if tj.exists():
            data = json.loads(tj.read_text(encoding="utf-8"))
            (folder / "tokens.css").write_text(token_css(data), encoding="utf-8")
            preset = {"id": meta["id"], "fontCss": meta.get("fontCss", [])}
            (folder / "tokens.js").write_text(
                "/* tools/build.py 가 만든다 */\nwindow.DS_TOKENS = " + json.dumps(data, ensure_ascii=False, indent=1) + ";\n"
                + "window.DS_PRESET = " + json.dumps(preset, ensure_ascii=False) + ";\n",
                encoding="utf-8")
            values = {t["name"]: t.get("value", t.get("light")) for g in data["groups"] for t in g["tokens"]}
            swatches = [values[n] for n in meta.get("swatches", []) if n in values]
        src = ROOT / meta["base"] if meta.get("base") else folder  # 프리셋은 기반 시스템의 카드·예시·한 장 보기를 쓴다
        cards = parse_cards(src / "cards", meta.get("cats", {}))
        examples = example_ids(src / "examples.js")
        for c in cards:
            c["example"] = c["id"] in examples
        systems.append({**meta, "swatches": swatches, "cards": cards, "dict": parse_dictionary(src / "dictionary.md"),
                        "home": src.name, "hasExamples": (src / "examples.html").exists(), "hasKit": (src / "kit.html").exists()})
        print(f"{meta['id']}: 카드 {len(cards)} · 예시 {len(examples)} · 사전 {len(systems[-1]['dict'])}" + (" · 토큰" if tj.exists() else "") + (f" · {meta['base']} 프리셋" if meta.get("base") else ""))
    out = {"built": date.today().isoformat(), "systems": systems}
    stamp_assets()
    (ROOT / "lookbook" / "catalog.js").write_text(
        "/* tools/build.py 가 만든다. 손으로 고치지 않는다 */\nwindow.DS_CATALOG = " + json.dumps(out, ensure_ascii=False, indent=1) + ";\n",
        encoding="utf-8")


def stamp_assets():
    """페이지가 부르는 css · js 주소 끝에 ?v=내용 해시를 붙인다. 내용이 바뀔 때만 달라져 브라우저가 새 파일을 받는다"""
    h = hashlib.sha1()
    for f in sorted(x for x in [*ROOT.glob("*/*.css"), *ROOT.glob("*/*.js")] if x.name != "catalog.js"):
        h.update(f.read_bytes())
    v = h.hexdigest()[:10]
    pat = re.compile(r'((?:href|src)="(?!https?:)[^"?]+\.(?:css|js))(?:\?v=\w+)?"')
    for page in [ROOT / "index.html", *ROOT.glob("*/kit.html"), *ROOT.glob("*/examples.html")]:
        t = page.read_text(encoding="utf-8")
        n = pat.sub(lambda m: m.group(1) + "?v=" + v + '"', t)
        if n != t:
            page.write_text(n, encoding="utf-8")


if __name__ == "__main__":
    main()
