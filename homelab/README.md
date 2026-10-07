# homelab

오라클 클라우드 무료 ARM 인스턴스 한 대로 운영하는 개인 서버의 설정 모음.
**인터넷에 열린 포트 없이** 운영하는 것이 목표다.

- 공개할 것은 Cloudflare Tunnel로 내보낸다 (서버가 바깥으로 먼저 연결).
- 나만 쓸 것은 Tailscale(tailnet) 안에서만 접근한다.
- OCI Security List에는 들어오는 포트가 없다 (ICMP만 허용).

일상 운영(페이지 추가, 새 공간, Cloudflare 관리, 문제 해결)은 [OPERATIONS.md](OPERATIONS.md).

## 구성

```
                                   ┌─ 오라클 서버 · 공개 포트 없음 ──────────────────────┐
[외부 사람] ──▶ [Cloudflare] ─Tunnel─▶ [cloudflared] ──▶ [Caddy 127.0.0.1:8080]          │
                                   │                         ├─ hwjs.ckaanf.com (비밀번호) │
                                   │                         └─ 그 외            404      │
                                   │                                                     │
[내 기기] ───▶ [Tailscale] ────────▶ [Uptime Kuma · Portainer · SSH]                     │
                                   │  [js 개인 공간 · 식단표 + 기록 API(SQLite)]          │
                                   └─────────────────────────────────────────────────────┘
```

ckaanf.com(허브)은 이 서버가 아니라 Cloudflare Workers가 서빙한다 ([ckaanf-hub](https://github.com/ckaanf/ckaanf-hub)).

| 주소 | 용도 | 접근 |
| --- | --- | --- |
| hwjs.ckaanf.com | 같이 보는 공간 | 비밀번호 (Caddy basicauth) |
| tailnet :8444 | 개인 공간 (식단표) | tailnet 전용 |
| tailnet :443 | Uptime Kuma | tailnet 전용 |
| tailnet :8443 | Portainer | tailnet 전용 |

## 폴더

| 경로 | 서버 위치 | 내용 |
| --- | --- | --- |
| `caddy/Caddyfile` | `/etc/caddy/Caddyfile` | 도메인별 사이트 블록 |
| `cloudflared/` | `/etc/cloudflared/` | Tunnel 연결 방법과 Public Hostname 목록 |
| `tailscale/` | — | tailnet 전용 서비스 노출 명령 |
| `docker/` | `~/uptime-kuma`, `~/portainer` | 관리 도구 compose 파일 |
| `fail2ban/jail.local` | `/etc/fail2ban/jail.local` | SSH 무차별 대입 차단 |
| `meal/` | `/etc/systemd/system/meal-*` | 식단표 배포·기록 동기화 API ([README](meal/README.md)) |
| `scripts/check.sh` | — | 전체 상태 점검 |

## 저장소에 넣지 않는 것

- **비밀 값:** Caddy 비밀번호 해시, Tunnel 토큰. Caddyfile에는 `{$HWJS_BASICAUTH_HASH}` 자리만 남겨 두었다.
- **사이트 내용:** `/var/www/*` 아래 페이지들은 개인 정보가 있어 따로 보관한다.
- **관리 도구 데이터:** Uptime Kuma의 `data/`, Portainer 볼륨.
- **식단 기록 DB:** `~/meal-data/meal.db`. 매일 `~/backups/meal`에 백업된다.

## 서버를 새로 만들 때

1. Ubuntu 24.04 인스턴스를 만들고, OCI Security List에서 들어오는 규칙을 모두 지운다 (처음 SSH용 22번만 잠시 허용).
2. Tailscale을 설치하고 tailnet에 붙인 뒤, SSH 접속을 tailnet 주소로 바꾸고 Security List의 22번 규칙을 지운다.
3. fail2ban을 설치하고 `fail2ban/jail.local`을 넣는다.
4. Caddy를 설치하고 `caddy/Caddyfile`을 넣는다. `{$HWJS_BASICAUTH_HASH}` 자리에 `caddy hash-password`로 만든 해시를 넣고 `sudo systemctl restart caddy`.
5. `/var/www/hwjs`, `/var/www/js`에 사이트 파일을 넣고 `sudo chmod -R a+rX /var/www`.
6. `cloudflared/README.md` 순서대로 Tunnel을 연결한다.
7. `docker/` 아래 compose를 올리고, `tailscale/README.md` 명령으로 tailnet에 노출한다.
8. `meal/README.md` 순서대로 식단표 서비스를 설치하고, 백업한 `meal.db`를 `~/meal-data/`에 되돌린다.
9. `scripts/check.sh`로 확인한다.

## 원칙

- sshd는 모든 주소에서 받지만 Security List(들어오는 규칙 없음), 키 전용 로그인, fail2ban으로 막는다. tailnet 주소에만 바인딩하면 Tailscale 장애 때 접속을 잃을 수 있어 그렇게 하지 않는다.
- 쓰지 않는 서비스가 외부 주소에 바인딩되지 않게 한다. `scripts/check.sh`의 노출 점검이 이를 확인한다.
- 관리 도구는 Cloudflare로 내보내지 않는다.
- 공개 범위가 바뀌는 작업(Funnel, Tunnel 호스트 추가, Caddy 사이트 주소 변경)은 적용 전에 임시 포트에서 먼저 시험한다.
- Tailscale Funnel은 포트 단위로 공개되므로, 같은 포트에 다른 서비스가 있으면 함께 공개된다. 지금은 쓰지 않는다.
