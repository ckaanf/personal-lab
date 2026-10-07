#!/usr/bin/env python3
"""식단 기록 DB를 날짜별로 백업한다. 30개를 넘으면 오래된 것부터 지운다."""
import os
import sqlite3
from datetime import date
from pathlib import Path

DB = Path(os.environ.get("MEAL_DB", "~/meal-data/meal.db")).expanduser()
DEST = Path(os.environ.get("MEAL_BACKUP_DIR", "~/backups/meal")).expanduser()
KEEP = 30

DEST.mkdir(parents=True, exist_ok=True)
target = DEST / f"meal-{date.today():%Y%m%d}.db"
src, dst = sqlite3.connect(DB), sqlite3.connect(target)
with dst:
    src.backup(dst)  # 서버가 쓰는 중이어도 일관된 사본을 만든다
src.close()
dst.close()
for old in sorted(DEST.glob("meal-*.db"))[:-KEEP]:
    old.unlink()
print(f"backup: {target}")
