# homelab

오라클 클라우드 무료 ARM 인스턴스 한 대로 운영하는 개인 서버의 설정 모음.
**인터넷에 열린 포트 없이** 운영하는 것이 목표다.

- 공개할 것은 Cloudflare Tunnel로 내보낸다 (서버가 바깥으로 먼저 연결).
- 나만 쓸 것은 Tailscale(tailnet) 안에서만 접근한다. 주소는 내 도메인(`*.ckaanf.com`)을 쓰고, DNS가 tailnet IP를 가리킨다.
- OCI Security List에는 들어오는 포트가 없다 (ICMP만 허용).

일상 운영(페이지 추가, 새 서비스, Cloudflare 관리, 문제 해결)은 [OPERATIONS.md](OPERATIONS.md).

## 구성

```
                                ┌─ 오라클 서버 · 공개 포트 없음 ───────────────────────────────┐
[외부 사람] ─▶ [Cloudflare] ─Tunnel─▶ [cloudflared] ─▶ [Caddy 127.0.0.1:8080]                │
                                │                        ├─ hwjs.ckaanf.com   비밀번호        │
                                │                        └─ 그 외             404             │
                                │                                                            │
[내 기기] ─▶ [Tailscale] ────────▶ [Caddy tailnet IP:443]  *.ckaanf.com 와일드카드 인증서     │
                                │                        ├─ js.ckaanf.com        개인 공간     │
                                │                        ├─ kuma.ckaanf.com      Uptime Kuma   │
                                │                        ├─ portainer.ckaanf.com Portainer     │
                                │                        └─ 그 외                404           │
                                └────────────────────────────────────────────────────────────┘
```

ckaanf.com(허브)은 이 서버가 아니라 Cloudflare Workers가 서빙한다 ([ckaanf-hub](https://github.com/ckaanf/ckaanf-hub)).

| 주소 | 용도 | 접근 |
| --- | --- | --- |
| hwjs.ckaanf.com | 같이 보는 공간 | 공개 + 비밀번호 (Tunnel → Caddy `basic_auth`) |
| js.ckaanf.com | 개인 공간 (식단표) | tailnet 전용 |
| kuma.ckaanf.com | Uptime Kuma | tailnet 전용 |
| portainer.ckaanf.com | Portainer | tailnet 전용 |

tailnet 전용 주소는 공개 DNS에 A 레코드(DNS 전용, 프록시 없음)로 tailnet IP를 가리킨다. 이름은 누구나 조회할 수 있지만 tailnet 밖에서는 접속되지 않는다. 인증서는 Caddy가 Cloudflare DNS 인증으로 `*.ckaanf.com` 하나를 받는다.

## 폴더

| 경로 | 서버 위치 | 내용 |
| --- | --- | --- |
| `caddy/Caddyfile` | `/etc/caddy/Caddyfile` | 공개(Tunnel)·tailnet 입구, 도메인별 블록 |
| `caddy/caddy.service.override.conf` | `/etc/systemd/system/caddy.service.d/override.conf` | Cloudflare 토큰 읽기, Tailscale 뒤에 시작 |
| `cloudflared/` | `/etc/cloudflared/` | Tunnel 연결 방법과 Public Hostname 목록 |
| `tailscale/` | — | tailnet 연결과 주의할 점 |
| `docker/` | `~/uptime-kuma`, `~/portainer` | 관리 도구 compose 파일 |
| `fail2ban/jail.local` | `/etc/fail2ban/jail.local` | SSH 무차별 대입 차단 |
| (비공개) `ckaanf-rooms` | `~/project/ckaanf-rooms` | 개인 공간(hwjs, js)의 목록 페이지와 식단표 배포·기록 동기화 앱 |
| `scripts/check.sh` | — | 전체 상태 점검 |

## 저장소에 넣지 않는 것

- **비밀 값:** Caddy 비밀번호 해시, Cloudflare API 토큰, Tunnel 토큰. Caddyfile에는 `{$HWJS_BASICAUTH_HASH}`, `{$TAILNET_IP}` 자리만 남겨 두었다.
- **사이트 내용:** `/var/www/*` 아래 페이지들은 개인 정보가 있어 따로 보관한다.
- **관리 도구 데이터:** Uptime Kuma의 `data/`, Portainer 볼륨.
- **식단 기록 DB:** `~/meal-data/meal.db`. 매일 `~/backups/meal`에 백업된다.

## 서버를 새로 만들 때

1. Ubuntu 24.04 인스턴스를 만들고, OCI Security List에서 들어오는 규칙을 모두 지운다 (처음 SSH용 22번만 잠시 허용).
2. Tailscale을 설치하고 tailnet에 붙인 뒤, SSH 접속을 tailnet 주소로 바꾸고 Security List의 22번 규칙을 지운다.
3. fail2ban을 설치하고 `fail2ban/jail.local`을 넣는다.
4. Caddy를 설치한다.
    1. apt로 설치한 뒤, Cloudflare DNS 모듈이 들어간 공식 빌드로 실행 파일을 바꾸고 apt가 덮어쓰지 않게 고정한다 (`curl -fL -o caddy 'https://caddyserver.com/api/download?os=linux&arch=arm64&p=github.com%2Fcaddy-dns%2Fcloudflare'` → `/usr/bin/caddy`, `sudo apt-mark hold caddy`).
    2. Cloudflare API 토큰(ckaanf.com의 Zone Read + DNS Edit만, 서버 IP 제한)을 `/etc/caddy/cloudflare.env`에 `CF_API_TOKEN=...`로 넣는다 (`root:caddy`, 640).
    3. `caddy/caddy.service.override.conf`를 넣는다. 기본 유닛의 `--environ`은 환경 변수(토큰)를 로그에 찍으므로 이 파일이 그걸 뺀다.
    4. `caddy/Caddyfile`을 넣고, `{$HWJS_BASICAUTH_HASH}`는 `caddy hash-password`로 만든 해시로, `{$TAILNET_IP}`는 `tailscale ip -4` 값으로 바꾼다. `sudo systemctl daemon-reload && sudo systemctl restart caddy`.
5. `/var/www/hwjs`, `/var/www/js`에 사이트 파일을 넣고 `sudo chmod -R a+rX /var/www`.
6. `cloudflared/README.md` 순서대로 Tunnel을 연결한다.
7. Cloudflare DNS에 `js`, `kuma`, `portainer` A 레코드를 새 tailnet IP로 고친다 (DNS 전용).
8. `docker/` 아래 compose를 올린다.
9. 비공개 저장소 `ckaanf-rooms`를 `~/project/`에 받고, `js/meal/README.md` 순서대로 식단표 서비스를 설치한다. 백업한 `meal.db`는 `~/meal-data/`에 되돌린다.
10. `scripts/check.sh`로 확인한다.

## 원칙

- sshd는 모든 주소에서 받지만 Security List(들어오는 규칙 없음), 키 전용 로그인, fail2ban으로 막는다. tailnet 주소에만 바인딩하면 Tailscale 장애 때 접속을 잃을 수 있어 그렇게 하지 않는다.
- 쓰지 않는 서비스가 외부 주소에 바인딩되지 않게 한다. `scripts/check.sh`의 노출 점검이 이를 확인한다.
- 관리 도구는 Cloudflare로 내보내지 않는다.
- 입구는 Caddy 하나다. 공개는 `127.0.0.1:8080`(Tunnel 뒤), tailnet은 tailnet IP의 443. `tailscale serve`와 Funnel은 쓰지 않는다.
- 공개 범위가 바뀌는 작업은 적용 전에 임시 포트에서 먼저 시험한다.
