#!/usr/bin/env bash
# 서버 전체 상태 점검. 서버에서 실행한다. 실패한 항목이 있으면 종료 코드 1.
set -u
fail=0

ok()  { printf '  ok    %s\n' "$1"; }
bad() { printf '  FAIL  %s\n' "$1"; fail=1; }

expect() {  # expect <설명> <기대 코드> <curl 인자...>
  local name=$1 want=$2; shift 2
  local got
  got=$(curl -s -o /dev/null -w '%{http_code}' --max-time 15 "$@" || true)
  [ "$got" = "$want" ] && ok "$name ($got)" || bad "$name (기대 $want, 실제 $got)"
}

echo "서비스"
for s in caddy cloudflared tailscaled fail2ban docker meal-api meal-publish.path meal-backup.timer; do
  systemctl is-active --quiet "$s" && ok "$s" || bad "$s"
done

echo "컨테이너"
for c in uptime-kuma portainer; do
  [ "$(docker inspect -f '{{.State.Running}}' "$c" 2>/dev/null)" = "true" ] && ok "$c" || bad "$c"
done

echo "Caddy (로컬, 도메인별 분리)"
expect "hwjs 비밀번호 없이 차단" 401 -H 'Host: hwjs.ckaanf.com' http://127.0.0.1:8080/
expect "공개 쪽 js 도메인 거절"   404 -H 'Host: js.ckaanf.com'   http://127.0.0.1:8080/
expect "모르는 도메인 거절"      404 -H 'Host: example.com'     http://127.0.0.1:8080/
expect "식단 API (로컬)"          200 http://127.0.0.1:8083/meal/api/doc/meal-2026-10-v2

echo "tailnet 입구 (Caddy tailnet IP:443, 와일드카드 인증서)"
TS=$(tailscale ip -4 2>/dev/null)
tn() { expect "$1" "$2" --resolve "$3:443:$TS" "https://$3$4"; }
tn "js 식단표"                  200 js.ckaanf.com        /meal/
tn "js 식단 API"                200 js.ckaanf.com        /meal/api/doc/meal-2026-10-v2
tn "kuma"                      302 kuma.ckaanf.com      /
tn "portainer"                 200 portainer.ckaanf.com /
tn "모르는 이름 거절"            404 nope.ckaanf.com      /
for n in js kuma portainer; do
  ip=$(dig +short "$n.ckaanf.com" @1.1.1.1 | tail -1)
  [ "$ip" = "$TS" ] && ok "DNS $n.ckaanf.com → tailnet IP" || bad "DNS $n.ckaanf.com → '$ip' (tailnet IP와 다름)"
done

echo "공개 주소"
expect "ckaanf.com (허브)"       200 https://ckaanf.com/
expect "hwjs.ckaanf.com"         401 https://hwjs.ckaanf.com/
expect "http → https 리다이렉트" 301 http://ckaanf.com/

echo "노출 점검 (127.0.0.1과 tailnet 밖으로 열린 포트가 없어야 함)"
open=$(ss -ltnH | awk '{print $4}' | grep -vE '^(127\.0\.0\.[0-9]+(%lo)?|\[::1\]|100\.[0-9.]+|\[fd7a:[^]]*\]):' | grep -vE ':22$' || true)
[ -z "$open" ] && ok "외부 바인딩 없음 (SSH 22 제외)" || bad "외부 바인딩: $(echo $open)"

exit $fail
