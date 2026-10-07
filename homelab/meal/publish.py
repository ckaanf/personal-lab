#!/usr/bin/env python3
"""볼트의 식단표 HTML을 웹으로 배포한다. 볼트 원본은 고치지 않는다.

- 원본: ~/vault/Vault/재정/*식단표*.html (맥 옵시디언 볼트가 Mutagen으로 동기화됨)
- 월 이름은 파일 안의 저장 키 "meal-YYYY-MM"에서 읽는다. 파일 이름에 연도가 없어도 된다.
- 배포: /var/www/js/meal/YYYY-MM/index.html (+ sync.js 한 줄 삽입), /var/www/js/meal/index.html (월 목록)
- 내용이 같으면 쓰지 않는다. systemd path 유닛이 원본이 바뀔 때마다 실행한다.
"""
import html
import os
import re
import shutil
import sys
from datetime import datetime, timedelta, timezone
from pathlib import Path

SRC = Path(os.environ.get("MEAL_SRC", "~/vault/Vault/재정")).expanduser()
OUT = Path(os.environ.get("MEAL_OUT", "/var/www/js/meal"))
HERE = Path(__file__).resolve().parent
KEY_RE = re.compile(r'["\']meal-(\d{4})-(\d{2})')
TITLE_RE = re.compile(r"<title>([^<]*)</title>", re.I)
INJECT = '<script src="/meal/sync.js"></script>'
KST = timezone(timedelta(hours=9))


def write_if_changed(path, text):
    path.parent.mkdir(parents=True, exist_ok=True)
    if path.exists() and path.read_text(encoding="utf-8") == text:
        return False
    tmp = path.with_suffix(".tmp")
    tmp.write_text(text, encoding="utf-8")
    tmp.chmod(0o644)
    tmp.replace(path)
    return True


def inject(page):
    # 페이지 스크립트보다 먼저 실행돼야 한다. charset 선언이 있으면 그 바로 뒤에 넣는다.
    m = re.search(r"<meta[^>]*charset[^>]*>", page, re.I) or re.search(r"<head[^>]*>", page, re.I)
    if not m:
        raise ValueError("<head> 없음")
    return page[: m.end()] + "\n" + INJECT + page[m.end():]


def index_page(months):
    items = "\n".join(
        f'    <li><a href="/meal/{m}/"><strong>{html.escape(t)}</strong><span>{m}</span></a></li>'
        for m, t in months)
    return f"""<!doctype html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>식단표</title>
<style>
:root {{ --bg:#f4f6f4; --surface:#fff; --ink:#18221e; --muted:#5d6b65; --line:#d9e0dc; --accent:#2c6a58; }}
@media (prefers-color-scheme: dark) {{ :root {{ --bg:#111614; --surface:#19201d; --ink:#e6ece9; --muted:#9aa8a2; --line:#2c3632; --accent:#7cc2ab; color-scheme:dark; }} }}
* {{ box-sizing:border-box; }}
body {{ margin:0; background:var(--bg); color:var(--ink); font:15px/1.6 system-ui,-apple-system,"Apple SD Gothic Neo",sans-serif; }}
main {{ max-width:560px; margin:0 auto; padding:40px 16px; }}
h1 {{ font-size:24px; margin:0 0 24px; }}
ul {{ list-style:none; margin:0; padding:0; display:grid; gap:10px; }}
a {{ display:flex; justify-content:space-between; gap:12px; padding:14px 18px; background:var(--surface); border:1px solid var(--line); border-radius:12px; color:inherit; text-decoration:none; }}
a:hover, a:focus-visible {{ border-color:var(--accent); outline:none; }}
span {{ color:var(--muted); font-variant-numeric:tabular-nums; }}
</style>
<script>
// 이번 달 식단표가 있으면 바로 연다. 목록을 보려면 /meal/#list
(function () {{
  if (location.hash === "#list") return;
  var d = new Date(Date.now() + 9 * 3600e3), m = d.toISOString().slice(0, 7);
  var months = {[m for m, _ in months]!r};
  if (months.indexOf(m) >= 0) location.replace("/meal/" + m + "/");
}})();
</script>
</head>
<body>
<main>
  <h1>식단표</h1>
  <ul>
{items}
  </ul>
</main>
</body>
</html>
"""


def main():
    months, changed = [], []
    for src in sorted(SRC.glob("*식단표*.html")):
        page = src.read_text(encoding="utf-8")
        m = KEY_RE.search(page)
        if not m:
            print(f"publish: 건너뜀 (저장 키 meal-YYYY-MM 없음): {src.name}", file=sys.stderr)
            continue
        month = f"{m.group(1)}-{m.group(2)}"
        t = TITLE_RE.search(page)
        months.append((month, t.group(1).strip() if t else src.stem))
        if write_if_changed(OUT / month / "index.html", inject(page)):
            changed.append(month)
    if not months:
        sys.exit("publish: 식단표를 하나도 찾지 못함")
    months.sort(reverse=True)
    sync_src = (HERE / "sync.js").read_text(encoding="utf-8")
    if write_if_changed(OUT / "sync.js", sync_src):
        changed.append("sync.js")
    if write_if_changed(OUT / "index.html", index_page(months)):
        changed.append("index")
    now = datetime.now(KST).strftime("%F %T")
    print(f"publish {now}: {', '.join(changed) if changed else '변경 없음'} (월: {', '.join(m for m, _ in months)})")


if __name__ == "__main__":
    main()
