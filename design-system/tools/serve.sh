#!/bin/bash
# 룩북을 로컬에서 연다: 빌드 → 서버(127.0.0.1:8791) → 기본 브라우저. 끄려면 Ctrl+C.
#   design-system/tools/serve.sh [포트]
set -euo pipefail
ROOT=$(cd "$(dirname "$0")/.." && pwd)
PORT=${1:-8791}
python3 "$ROOT/tools/build.py"
if lsof -nP -iTCP:"$PORT" -sTCP:LISTEN >/dev/null 2>&1; then
  echo "이미 $PORT 에서 떠 있음 → 브라우저만 연다"
  open "http://127.0.0.1:$PORT/"
  exit 0
fi
(sleep 1; open "http://127.0.0.1:$PORT/") &
echo "룩북: http://127.0.0.1:$PORT/  (끄기: Ctrl+C)"
exec python3 -m http.server "$PORT" --bind 127.0.0.1 --directory "$ROOT"
