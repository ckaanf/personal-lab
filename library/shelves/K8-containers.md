# K8 · 컨테이너·쿠버네티스

### K8-01 · 컨테이너는 무엇인가 (cgroups·네임스페이스)
- 장면: 컨테이너 안에서는 CPU가 8개로 보이는데 실제로는 2개만 씀.
- 이론: 네임스페이스(격리)와 cgroups(제한), 이미지 레이어, 컨테이너는 가벼운 VM이 아니다.
- 답해 볼 것: ① CPU 한도와 쓰로틀링 ② 메모리 한도를 넘으면 ③ JVM이 컨테이너 한도를 읽는 방식
- 읽을 곳: Docker 문서 "Resource constraints" · `man 7 cgroups`
- 실습: CPU 한도 0.5·1·2로 같은 작업 → 시간·쓰로틀링 횟수 예측 후 측정
- 손으로: 최소 이미지 Dockerfile (멀티 스테이지)
- 끝: 볼트에 3줄

### K8-02 · 프로브와 무중단
- 장면: 배포할 때마다 몇 초씩 502 / 느린 시작 앱이 계속 재시작됨.
- 이론: startup·readiness·liveness의 차이, graceful shutdown(preStop, SIGTERM), 연결 드레이닝.
- 답해 볼 것: ① liveness에 DB 확인을 넣으면 ② SIGTERM을 받은 앱이 할 일 ③ readiness가 실패하면 트래픽은
- 읽을 곳: Kubernetes 문서 "Configure Liveness, Readiness and Startup Probes"
- 실습: 롤링 배포 중 요청을 계속 보내며 실패 수 측정 → 설정 고치고 0 만들기
- 손으로: graceful shutdown 설정
- 끝: 볼트에 3줄

### K8-03 · 리소스 요청·한도와 스케줄링
- 장면: 노드는 남는데 파드가 Pending / 한 파드가 노드를 다 먹음.
- 이론: requests(스케줄 기준) vs limits(상한), QoS 클래스, 축출 순서, HPA.
- 답해 볼 것: ① requests 없이 띄우면 ② CPU limit을 거는 것의 손해 ③ HPA가 늦게 반응하는 이유
- 읽을 곳: Kubernetes 문서 "Resource Management for Pods and Containers"
- 실습: 로컬 클러스터(kind)에서 requests·limits 조합별 스케줄·축출 관찰
- 손으로: 내 앱의 requests·limits와 근거
- 끝: 볼트에 3줄

### K8-04 · 설정·비밀·배포 도구
- 장면: 환경마다 설정 파일이 달라 운영에만 버그.
- 이론: ConfigMap·Secret, 12-factor 설정, Helm·Kustomize, GitOps(ArgoCD) — 원하는 상태를 깃에.
- 답해 볼 것: ① Secret이 암호화가 아닌 이유 ② 설정 변경을 재시작 없이 반영하려면 ③ GitOps가 막는 사고
- 읽을 곳: "The Twelve-Factor App" III. Config · ArgoCD 문서 "Core Concepts"
- 실습: kind + ArgoCD로 깃 변경이 반영되는 흐름, 손으로 바꾼 설정이 되돌려지는 것 확인
- 손으로: 환경별 오버레이(Kustomize)
- 끝: 볼트에 3줄
