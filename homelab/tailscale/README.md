# tailscale

서버를 tailnet에 붙이고, tailnet 전용 서비스는 **Caddy가 tailnet IP의 443번에서** 받는다.
`tailscale serve`는 쓰지 않는다 (2026-10-07까지 Kuma·Portainer·js에 썼다).

- tailnet IP는 `tailscale ip -4`로 확인한다. Caddyfile의 `{$TAILNET_IP}`와 Cloudflare DNS의 A 레코드(`js`, `kuma`, `portainer`)가 이 값을 가리켜야 한다. 서버를 새로 붙여 IP가 바뀌면 둘 다 고친다.
- Caddy는 tailnet IP에 바인딩하므로 Tailscale보다 먼저 뜨면 실패한다. `caddy.service.override.conf`가 순서와 재시도를 맡는다.
- `tailscale serve`를 다시 켜면 tailnet IP의 같은 포트를 두고 Caddy와 부딪힌다.
- `tailscale funnel`은 쓰지 않는다. Funnel은 포트 단위로 인터넷에 공개되어 같은 포트의 서비스가 함께 노출된다.
- fail2ban은 tailnet 대역(`100.64.0.0/10`)을 차단 대상에서 뺀다.
