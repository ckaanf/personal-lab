#!/usr/bin/env python3
"""디자인 시스템 빌드 — 시스템 폴더(system.json 이 있는 곳)마다

  tokens.json  → tokens.css(밝은/어두운 모드) · tokens.js(조종 패널용)
  cards/*.md   → lookbook/catalog.js(룩북이 읽는 블록 목록)

를 만든다. 표준 라이브러리만 쓴다. 실행: python3 tools/build.py
"""
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
            (folder / "tokens.js").write_text(
                "/* tools/build.py 가 만든다 */\nwindow.DS_TOKENS = " + json.dumps(data, ensure_ascii=False, indent=1) + ";\n",
                encoding="utf-8")
            values = {t["name"]: t.get("value", t.get("light")) for g in data["groups"] for t in g["tokens"]}
            swatches = [values[n] for n in meta.get("swatches", []) if n in values]
        cards = parse_cards(folder / "cards", meta.get("cats", {}))
        examples = example_ids(folder / "examples.js")
        for c in cards:
            c["example"] = c["id"] in examples
        systems.append({**meta, "swatches": swatches, "cards": cards,
                        "hasExamples": (folder / "examples.html").exists(), "hasKit": (folder / "kit.html").exists()})
        print(f"{meta['id']}: 카드 {len(cards)} · 예시 {len(examples)}" + (" · 토큰" if tj.exists() else ""))
    out = {"built": date.today().isoformat(), "systems": systems}
    (ROOT / "lookbook" / "catalog.js").write_text(
        "/* tools/build.py 가 만든다. 손으로 고치지 않는다 */\nwindow.DS_CATALOG = " + json.dumps(out, ensure_ascii=False, indent=1) + ";\n",
        encoding="utf-8")


if __name__ == "__main__":
    main()
