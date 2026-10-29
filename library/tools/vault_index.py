#!/usr/bin/env python3
"""서가(shelves/*.md)에서 볼트 목차(`도서관.md`)를 만든다. 카드를 더하거나 고치면 다시 돌린다.
쓰는 법: python3 library/tools/vault_index.py > <볼트>/도서관.md   (볼트: 회사 맥 ~/Personal/vault, 서버 ~/vault/Vault)
"""
import posixpath, re, pathlib

LIB = pathlib.Path(__file__).resolve().parent.parent
BASE = 'https://github.com/ckaanf/personal-lab/blob/main/'

def url(rel):  # library/ 기준 상대 경로 → GitHub 주소
    return BASE + posixpath.normpath(posixpath.join('library', rel))

print('''---
created: 2026-10-29
---
# 도서관 (목차)

> 원본은 personal-lab 레포 `library/` — 카드 내용은 거기서 본다. 이 장은 **꺼내 볼 목차**와 **내가 한 기록**을 잇는 곳.
> 이 장은 `library/tools/vault_index.py`로 만든다. 손으로 고치지 말고 레포 카드를 고친 뒤 다시 만든다.
> 쓰는 법: 카드를 하나 고른다 → 레포에서 이론 → 실습(예측하고 재기) → `Notes/실험기록/`에 3줄, 맨 위에 `library: 카드ID`.
> 지금 꺼낸 카드는 [[방향]] 학습 줄에 한 줄 (한 번에 하나).

## 내가 남긴 기록
```query
"library:"
```''')
readme = (LIB / 'README.md').read_text(encoding='utf-8')
shelves = readme[readme.index('## 서가\n'):readme.index('## 카드 모양')]  # 서가 표 부분만
for line in shelves.splitlines():
    if line.startswith('### '):  # 서가 묶음
        print(f"\n## {line[4:]}")
        continue
    if not (line.startswith('| ') and '](shelves/' in line):
        continue
    c = [x.strip() for x in line.strip().strip('|').split('|')]
    shelf = re.search(r'\[(.*?)\]\((.*?)\)', c[1]); lab = re.search(r'\[(.*?)\]\((.*?)\)', c[3])
    print(f"\n### {c[0]} · {shelf.group(1)}" + (f" — 깊게: [{lab.group(1)}]({url(lab.group(2))})" if lab else ''))
    print('| ID | 카드 | 장면 |\n|---|---|---|')
    text = (LIB / shelf.group(2)).read_text(encoding='utf-8')
    for cid, title, scene in re.findall(r'^### (\S+) · (.+)\n- 장면: (.+)$', text, re.M):
        print(f"| {cid} | [{title}]({url(shelf.group(2))}) | {scene} |")
