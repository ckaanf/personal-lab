# tailscale

관리 도구는 127.0.0.1에만 바인딩하고, `tailscale serve`로 tailnet 안에서만 HTTPS로 노출한다.

```bash
sudo tailscale serve --bg --https=443 http://127.0.0.1:3001         # Uptime Kuma
sudo tailscale serve --bg --https=8443 https+insecure://127.0.0.1:9443  # Portainer
sudo tailscale serve --bg --https=8444 http://127.0.0.1:8082         # js 개인 공간 (식단표)
```

```bash
sudo tailscale serve status
```

- 주소는 `https://<기기 이름>.<tailnet>.ts.net`, `:8443`, `:8444`이다.
- `tailscale funnel`은 쓰지 않는다. Funnel은 포트 단위로 인터넷에 공개되어 같은 포트의 서비스가 함께 노출된다.
- fail2ban은 tailnet 대역(`100.64.0.0/10`)을 차단 대상에서 뺀다.
