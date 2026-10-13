#!/bin/bash
# Uptime Kuma Push 모니터에 상태를 보낸다. kuma-push.timer가 1분마다 실행.
# Push URL은 ~/.config/kuma-push.env (이름="https://.../api/push/<토큰>?..."), 저장소에 넣지 않는다.
# 토큰만 꺼내 127.0.0.1:3001로 보낸다 (Caddy·Tailscale 상태와 무관하게).
set -u
ENV_FILE="${KUMA_PUSH_ENV:-$HOME/.config/kuma-push.env}"
KUMA="http://127.0.0.1:3001"

token() {
  sed -n "s#^$1=\"\{0,1\}[^\"]*/api/push/\([A-Za-z0-9]*\).*#\1#p" "$ENV_FILE"
}

push() {  # 이름 상태(up|down) 메시지
  local t; t=$(token "$1")
  [ -n "$t" ] || { echo "토큰 없음: $1" >&2; return; }
  curl -fsS -o /dev/null -m 10 -G "$KUMA/api/push/$t" \
    --data-urlencode "status=$2" --data-urlencode "msg=$3" --data-urlencode "ping=" \
    || echo "전송 실패: $1" >&2
}

user_unit() {  # 이름 유닛
  local s; s=$(systemctl --user is-active "$2")
  [ "$s" = active ] && push "$1" up OK || push "$1" down "$2: $s"
}

user_unit claude-rc claude-rc
user_unit obsidian-sync obsidian-sync
tmux has-session -t cc 2>/dev/null && push tmux-cc up OK || push tmux-cc down "tmux 세션 cc 없음"
