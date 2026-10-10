# CLAUDE.md — 에이전트 작업 안내 (homelab)

- 한국어로 답한다. 서버나 Cloudflare 설정을 바꾸기 전에 단계별 계획을 먼저 보여 주고 확인받는다.
- **[OPERATIONS.md](OPERATIONS.md)가 운영 문서의 원본이다.** 설정을 바꾸면 해당 부분과 맨 아래 변경 기록을 함께 고친다. 구성 개요와 재구성 순서는 [README.md](README.md).
- **공개 저장소다.** 비밀 값(비밀번호, 해시, 토큰, Deploy hook URL), tailnet 주소, 사이트 내용은 적지 않는다.
- 공개 범위가 바뀌는 변경은 임시 포트에서 먼저 시험하고, 적용 명령은 사람이 승인한다 (OPERATIONS.md "원칙").
- 서버 접속: 맥에서 `ssh oracle-arm` (Tailscale). 서버 사본은 `~/project/personal-lab`.
- 개인 공간 앱(식단표 등)의 코드와 작업 안내는 비공개 저장소 `ckaanf-rooms`의 `CLAUDE.md`에 있다.
