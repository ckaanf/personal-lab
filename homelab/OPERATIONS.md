# 운영 문서

ckaanf.com과 오라클 서버를 운영하는 방법. **이 파일이 운영 문서의 원본이다.**
구성 개요와 서버 재구성 순서는 [README.md](README.md)에 있다.

- 에이전트에게: 서버나 Cloudflare 설정을 바꾸면 이 문서의 해당 부분과 맨 아래 변경 기록을 함께 고친다.
- 공개 저장소다. 비밀 값(비밀번호, 해시, 토큰, Deploy hook URL), tailnet 주소, 사이트 내용은 적지 않는다.

## 사이트

| 주소 | 용도 | 서빙 | 접근 |
| --- | --- | --- | --- |
| ckaanf.com | 허브(포트폴리오) | Cloudflare Workers | 공개 |
| hwjs.ckaanf.com | 같이 보는 공간 | 서버 Caddy (127.0.0.1:8080, Tunnel 뒤) | 비밀번호 (basic_auth) |
| js.ckaanf.com | 개인 공간 (식단표) | 서버 Caddy (tailnet IP:443) | tailnet 전용 |
| kuma.ckaanf.com | Uptime Kuma | 서버 Caddy (tailnet IP:443) | tailnet 전용 |
| portainer.ckaanf.com | Portainer | 서버 Caddy (tailnet IP:443) | tailnet 전용 |

| 항목 | hwjs | js (개인 공간) |
| --- | --- | --- |
| 파일 | `/var/www/hwjs/` | `/var/www/js/` |
| 목록 페이지 | `index.html` (noindex) | `index.html` (noindex) |
| 페이지 | `/jeonse/` | `/meal/` → 이번 달 식단표 (`/meal/YYYY-MM/`) |
| 접근 제어 | Caddy `basic_auth` (bcrypt) | tailnet 전용 (DNS가 tailnet IP를 가리킴) |

폴더 하나가 페이지 하나다. 사이트 내용은 이 저장소에 넣지 않는다.

식단표는 볼트(`재정/*식단표*.html`)가 원본이고, 바뀌면 자동으로 배포된다. 기록은 SQLite에 저장되어 기기끼리 동기화된다. 개인 공간의 코드는 비공개 저장소 `ckaanf-rooms`(서버 사본 `~/project/ckaanf-rooms`)에 있다.

## 구성 요소

| 구성 요소 | 위치 | 비고 |
| --- | --- | --- |
| Caddy 설정 | `/etc/caddy/Caddyfile` | 저장소의 `caddy/Caddyfile`과 같음 (해시와 tailnet IP만 다름) |
| Caddy 백업 | `/etc/caddy/Caddyfile.orig`, `.bak-20261007*` | 설치 직후 원본, 이후 바꾸기 직전마다 |
| Caddy 서비스 | `caddy` (systemd), 127.0.0.1:8080(공개, Tunnel 뒤) + tailnet IP:443 | 서비스 설정 `/etc/systemd/system/caddy.service.d/override.conf`. v2.11 + Cloudflare DNS 모듈(공식 빌드), `apt-mark hold caddy`. apt판 2.6.2는 `/usr/local/lib/caddy-2.6.2-apt`에 보관. 관리 API 꺼짐(`admin off`) |
| Tunnel 커넥터 | `cloudflared` (systemd) | Tunnel 이름 `oracle-arm`, 토큰은 `/etc/cloudflared/token` |
| 관리 도구 | `~/uptime-kuma`(127.0.0.1:3001), `~/portainer`(127.0.0.1:9443) (docker compose) | Caddy를 거쳐 tailnet 전용 |
| Cloudflare API 토큰 | Caddy용 `/etc/caddy/cloudflare.env`, 에이전트용 `~/.config/cloudflare/agent.env` | Caddy용은 ckaanf.com DNS만, 에이전트용은 DNS·Tunnel·Workers·규칙(결제·Registrar·토큰 관리 제외). 둘 다 서버 IP 제한 |
| 식단 서비스 | `meal-api`(127.0.0.1:8083), `meal-publish.path`, `meal-backup.timer` | 코드 `~/project/ckaanf-rooms/js/meal`, DB `~/meal-data/meal.db`, 백업 `~/backups/meal` |
| 상태 점검 | `scripts/check.sh` | 서비스, 공개 주소, 외부 바인딩 |
| 서버 저장소 사본 | `~/project/personal-lab` | Deploy key로 push |

## 자주 하는 작업

명령은 모두 서버에서 실행한다.

### 페이지 추가

1. 맥에서 서버로 파일을 복사한다. 한글 파일명은 맥과 리눅스의 유니코드 정규화(NFD/NFC)가 달라 이름으로 안 잡힐 수 있으니 영문 이름으로 보낸다.
2. `/var/www/<공간>/<폴더>/index.html`로 옮기고 읽기 권한을 준다.
3. 그 공간의 `index.html` 목록에 카드 한 줄을 추가한다.

```bash
scp -i ~/.ssh/<OCI 키> page.html ubuntu@oracle-arm:/tmp/page.html
```

```bash
sudo mkdir -p /var/www/hwjs/<폴더> && sudo mv /tmp/page.html /var/www/hwjs/<폴더>/index.html && sudo chmod -R a+rX /var/www/hwjs
```

### 새 달 식단표

볼트 `재정/`에 새 식단표 HTML(저장 키 `meal-YYYY-MM-...`)을 넣으면 끝이다. 배포와 목록 갱신은 자동이고, 지난 달 기록은 남는다. 배포 로그는 `journalctl -u meal-publish`.

### tailnet 전용 서비스 추가

1. 서비스를 `127.0.0.1:<포트>`에만 띄운다.
2. `Caddyfile`의 `*.ckaanf.com` 블록에 `@<이름> host <이름>.ckaanf.com`과 `handle @<이름> { reverse_proxy 127.0.0.1:<포트> }`를 추가한다. 인증서는 와일드카드라 따로 받을 필요가 없다.
3. 서버에서 먼저 시험한다: `curl --resolve <이름>.ckaanf.com:443:$(tailscale ip -4) https://<이름>.ckaanf.com/`.
4. `sudo systemctl restart caddy`.
5. Cloudflare DNS에 A 레코드 `<이름>` → tailnet IP를 **DNS 전용(프록시 끔)**으로 추가한다.
6. 이 저장소의 `caddy/Caddyfile`, 이 문서, `scripts/check.sh`를 고친다.

### 새 공개 공간(서브도메인) 추가


1. `Caddyfile`에 `http://<이름>.ckaanf.com:8080 { bind 127.0.0.1 ... }` 블록을 추가한다.
2. 임시 포트에서 먼저 시험한다. 설정의 `:8080`을 `:18080`으로 바꾼 사본을 `caddy run`으로 띄우고 `curl -H 'Host: <이름>.ckaanf.com'`으로 확인한다.
3. 실제 설정에 적용하고 `sudo systemctl restart caddy`.
4. Cloudflare Tunnel에 Public Hostname `<이름>` → `http://127.0.0.1:8080`을 추가한다.
5. 이 저장소의 `caddy/Caddyfile`, 이 문서, `scripts/check.sh`를 고친다.

### 비밀번호 변경 (hwjs)

1. `caddy hash-password`로 새 해시를 만든다.
2. `/etc/caddy/Caddyfile`의 `basic_auth` 블록에서 해시를 교체한다.
3. `sudo systemctl restart caddy`. `admin off`라서 `reload`는 동작하지 않는다.

### 상태 점검

```bash
~/project/personal-lab/homelab/scripts/check.sh
```

## Cloudflare

돈이 드는 것은 도메인뿐이고, Tunnel·DNS·Workers는 Free 플랜 안에서 쓴다.

| 항목 | 대시보드 위치 | 할 일 |
| --- | --- | --- |
| 도메인 자동 연장 | Domain Registration → ckaanf.com | 켜져 있음. 결제 카드가 바뀌면 Billing에서 교체 |
| Tunnel 상태 | Zero Trust → Networks → Tunnels | `oracle-arm`이 Healthy인지 |
| Public Hostname | Tunnels → oracle-arm | 서브도메인 추가·삭제. URL은 `localhost`가 아니라 `127.0.0.1:8080` |
| DNS 레코드 | DNS → Records | Tunnel이 만든 CNAME(hwjs)은 Public Hostname에서 지운다. tailnet 전용 A 레코드(js, kuma, portainer)는 **프록시를 켜지 않는다** (켜면 Cloudflare가 tailnet IP에 닿지 못함) |
| API 토큰 | 프로필 → API Tokens | `oracle-arm-caddy-dns`(Caddy용), `oracle-arm-agent`(에이전트용, 2027-12-31 만료) |
| HTTPS 강제 | SSL/TLS → Edge Certificates | Always Use HTTPS 켜짐 |
| Tunnel 토큰 | Tunnels → oracle-arm | 유출되면 Refresh로 재발급 후 `cloudflared service install`로 재등록 |

## 허브 (ckaanf.com)

서버가 아니라 Cloudflare Workers가 서빙하므로 서버가 멈춰도 열린다. 자세한 내용은 [ckaanf-hub](https://github.com/ckaanf/ckaanf-hub) 저장소(비공개, 서버 사본 `~/project/ckaanf-hub`)의 README.

| 항목 | 값 |
| --- | --- |
| 사람이 고치는 파일 | `content/hub.toml` (TODO가 남으면 빌드 실패) |
| Worker 빌드 | Build `python3 build.py`, Deploy `npx wrangler deploy --config wrangler.jsonc` |
| 최근 글 | 티스토리 RSS, `일상` 카테고리 제외 |
| 재빌드 | GitHub Actions `refresh` 3시간마다 → Worker Deploy Hook |

- Build command를 비우거나 Deploy command에서 `--config`를 빼면 wrangler가 자동 설정으로 템플릿 원본을 배포한다. 빌드는 "성공"으로 끝나서 알아채기 어렵다.
- 새 글을 바로 반영하려면 GitHub Actions의 refresh에서 Run workflow.

## 원칙

- 보여줄 것은 Cloudflare Tunnel, 나만 쓸 것은 Tailscale. OCI Security List에는 들어오는 규칙이 없다.
- 관리 도구(Kuma, Portainer, SSH)는 Cloudflare로 내보내지 않는다.
- Tailscale Funnel은 쓰지 않는다. 포트 단위로 공개되어 같은 포트의 서비스가 함께 노출된다.
- 입구는 Caddy 하나다. 공개는 `127.0.0.1:8080`(Tunnel 뒤), tailnet은 tailnet IP의 443. 블록에 없는 도메인은 404로 거절한다.
- `tailscale serve`는 쓰지 않는다. tailnet IP의 포트를 두고 Caddy와 부딪힌다.
- 공개 범위가 바뀌는 변경은 임시 포트에서 먼저 시험하고, 적용 명령은 사람이 승인한다.

## 문제 해결

| 증상 | 원인 | 해결 |
| --- | --- | --- |
| 새 서브도메인이 404 | Caddyfile에 그 도메인 블록이 없음 | 블록 추가 후 restart |
| 502 / Cloudflare 에러 페이지 | Caddy가 꺼졌거나 Public Hostname URL이 `localhost` | `systemctl status caddy`, URL은 `127.0.0.1:8080` |
| 1033 / Tunnel 에러 | cloudflared가 꺼졌거나 토큰 변경 | `systemctl status cloudflared`, 토큰 재등록 |
| 페이지 403/404 | 파일 권한 또는 경로 | `sudo chmod -R a+rX /var/www/<공간>` |
| `systemctl reload caddy` 실패 | `admin off` | `restart` 사용 |
| 서버에서만 새 서브도메인 DNS가 안 풀림 | 추가 전에 조회한 NXDOMAIN이 상위 DNS에 캐시됨 | 외부에서 확인 (`dig @1.1.1.1`), 30분 안에 풀림 |
| tailnet 주소(js, kuma, portainer)가 안 열림 | 기기의 Tailscale이 꺼짐, 또는 A 레코드 IP가 서버 tailnet IP와 다름 | 기기에서 Tailscale 연결 확인, `tailscale ip -4`와 DNS 비교 |
| 인증서 오류 / Caddy 로그에 `obtaining certificate` 실패 | Caddy용 토큰이 없거나 권한·IP 제한이 맞지 않음 | `/etc/caddy/cloudflare.env` 확인, `journalctl -u caddy` |
| Caddy가 부팅 직후 실패 | tailnet IP가 아직 안 붙음 | override의 `Restart=on-failure`가 5초 뒤 다시 시도 |
| 맥 터미널 scp/ssh가 `Permission denied (publickey)` | 기본 키를 씀. 서버는 OCI 키만 허용 | 맥 `~/.ssh/config`에 `Host oracle-arm`, `User ubuntu`, `IdentityFile` 지정 |

## 변경 기록

| 날짜 | 변경 | 이유 |
| --- | --- | --- |
| 2026-10-07 | tailnet 입구를 `tailscale serve`에서 Caddy(tailnet IP:443)로 전환. js·kuma·portainer.ckaanf.com A 레코드(DNS 전용), `*.ckaanf.com` 와일드카드 인증서 | 내 도메인으로 tailnet 서비스를 부르고, 새 서비스를 블록 하나로 추가하려고 |
| 2026-10-07 | Caddy 서비스 설정에서 `--environ` 제거, Cloudflare 토큰 연결 | `--environ`이 토큰을 로그에 찍기 때문 |
| 2026-10-07 | Caddy를 Cloudflare DNS 모듈 포함 2.11로 교체, apt 고정 | DNS 인증으로 tailnet 전용 주소에도 인증서를 받으려고 |
| 2026-10-07 | 개인 공간 코드(식단 동기화)를 비공개 `ckaanf-rooms`로 이전 | 서버 인프라 설정이 아니라 개인 앱이라서 |
| 2026-10-07 | js 공간을 Tailscale 전용(:8444)으로 전환, 식단표 월별 배포·기록 동기화(SQLite) 추가 | 개인 기록을 여러 기기에서 쓰되 인터넷에는 열지 않으려고 |
| 2026-10-07 | 운영 문서 원본을 이 파일로 이전 | Claude 아티팩트는 다른 에이전트가 읽을 수 없어서 |
| 2026-10-07 | rpcbind(111번) 비활성화 | 점검 스크립트가 모든 주소에 열린 것을 발견, NFS 미사용 |
| 2026-10-07 | 서버 설정을 personal-lab/homelab에 백업 | 서버가 사라져도 재구성할 수 있게 |
| 2026-10-07 | js.ckaanf.com 추가, Caddy를 도메인별 블록으로 분리 | 공간마다 접근 정책을 따로 두려고 |
| 2026-10-07 | Always Use HTTPS 켬 | http로 들어와도 https로 넘기려고 |
| 2026-10-07 | 허브를 Cloudflare Workers로 배포 | 서버 장애에 명함 페이지가 묶이지 않게. Pages는 Workers로 통합 중 |
| 2026-10-07 | Tailscale Funnel 끄고 Cloudflare Tunnel로 전환 | 내 도메인 사용, Funnel은 커스텀 도메인 미지원 |
| 2026-10-07 | ckaanf.com 구매 (Cloudflare Registrar) | 연장가가 원가 |
| 2026-10-06 | Caddy + basicauth 도입 | nginx보다 설정이 짧고 bcrypt 기본 |
