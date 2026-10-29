# MS · 미디어 서버 구조

> MD(미디어 지연) 다음 층 — 송출을 받아 여러 사람에게 나눠 주는 서버 쪽.

### MS-01 · 받기: RTMP·SRT·WHIP
- 장면: OBS로 송출하는데 망이 흔들리면 방송이 끊김.
- 이론: RTMP(TCP, 오래된 표준), SRT(UDP + 재전송, 흔들리는 망에 강함), WHIP(WebRTC로 받기). 받기 지연과 안정성의 맞교환.
- 답해 볼 것: ① RTMP가 손실 많은 망에서 약한 이유 ② SRT 지연 설정이 하는 일 ③ 브라우저에서 바로 송출하려면
- 읽을 곳: SRT Alliance "SRT Protocol Technical Overview" · IETF WHIP 초안(RFC 9725)
- 실습: 같은 송출을 RTMP vs SRT로, `tc netem` 손실 1·5%에서 끊김·지연 측정
- 손으로: 받기 서버 설정(MediaMTX 등)
- 끝: 볼트에 3줄

### MS-02 · 트랜스코딩 파이프라인과 ABR 사다리
- 장면: 시청자 망에 따라 화면이 멈추는데 화질 선택지가 하나뿐.
- 이론: 해상도·비트레이트 사다리, 키프레임 정렬(전환 가능하게), 실시간 트랜스코딩의 CPU/GPU 비용, 패스스루.
- 답해 볼 것: ① 키프레임이 안 맞으면 화질 전환 때 ② 화질 3개로 늘리면 CPU는 ③ 사다리 칸을 정하는 기준
- 읽을 곳: Apple "HLS Authoring Specification" · Netflix Tech Blog "Per-Title Encode Optimization"
- 실습: FFmpeg 한 프로세스로 화질 3개 출력, 화질 수별 CPU 예측 후 측정 (Media-Lab 질문 1)
- 손으로: ABR 사다리 표
- 끝: 볼트에 3줄

### MS-03 · SFU 구조
- 장면: 화상 방에 사람이 늘수록 각자의 업로드가 터짐(메시 연결).
- 이론: 메시 vs MCU(섞어서 하나로) vs SFU(골라서 전달), 시뮬캐스트·SVC, 대역폭 추정과 계층 선택.
- 답해 볼 것: ① 6명 메시에서 한 사람의 업로드 개수 ② SFU가 디코딩하지 않는 이유 ③ 시뮬캐스트로 얻는 것
- 읽을 곳: webrtcforthecurious.com "Applied WebRTC" · LiveKit 문서 "Architecture"
- 실습: LiveKit(또는 Pion 예제) 로컬 SFU에 클라이언트 N명 → 서버 CPU·대역폭 예측 후 측정
- 손으로: 방 크기별 구조 선택표
- 끝: 볼트에 3줄

### MS-04 · 배포와 CDN
- 장면: 시청자가 몰리자 원본 서버 대역폭이 바닥.
- 이론: 원본(origin) — 엣지 캐시, 세그먼트 캐시 TTL, 매니페스트는 짧게, 원본 보호(shield), 저지연과 캐시의 긴장.
- 답해 볼 것: ① 매니페스트와 세그먼트 캐시 시간이 다른 이유 ② 시청자 1만 명이면 원본 요청은 ③ LL-HLS에서 CDN이 어려운 점
- 읽을 곳: Cloudflare 블로그 스트리밍 글 · Apple LL-HLS 문서
- 실습: nginx 캐시를 원본 앞에 두고 시청자 수별 원본 요청 수 측정
- 손으로: 캐시 규칙
- 끝: 볼트에 3줄
