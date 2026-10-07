# cloudflared

Cloudflare Tunnel 커넥터. 서버가 Cloudflare 쪽으로 먼저 연결하므로 들어오는 포트가 필요 없다.

## 설치 (Ubuntu, arm64)

```bash
sudo mkdir -p --mode=0755 /usr/share/keyrings
curl -fsSL https://pkg.cloudflare.com/cloudflare-main.gpg | sudo tee /usr/share/keyrings/cloudflare-main.gpg >/dev/null
echo 'deb [signed-by=/usr/share/keyrings/cloudflare-main.gpg] https://pkg.cloudflare.com/cloudflared any main' | sudo tee /etc/apt/sources.list.d/cloudflared.list
sudo apt-get update && sudo apt-get install -y cloudflared
```

## 연결

1. Cloudflare → Zero Trust → Networks → Tunnels에서 Tunnel(`oracle-arm`)의 토큰을 복사한다.
2. 서버에서 아래 명령으로 서비스를 등록한다. 토큰은 `/etc/cloudflared/token`에 저장되고 저장소에는 넣지 않는다.

```bash
sudo cloudflared service install <토큰>
```

토큰이 유출됐다면 대시보드에서 Refresh로 재발급하고 다시 등록한다.

## Public Hostname

| Hostname | Service |
| --- | --- |
| hwjs.ckaanf.com | `http://127.0.0.1:8080` |
| js.ckaanf.com | `http://127.0.0.1:8080` |

- `localhost` 대신 `127.0.0.1`을 쓴다. Caddy는 IPv4에만 바인딩하는데 `localhost`는 IPv6(`::1`)로 먼저 연결될 수 있다.
- 새 서브도메인은 여기에 추가하고, Caddyfile에도 같은 도메인 블록을 추가해야 한다. 블록이 없으면 404가 난다.
