# CA · 캐시

### CA-01 · 캐시 패턴과 무효화
- 장면: 가격을 바꿨는데 화면에는 옛 가격이 10분째.
- 이론: cache-aside, write-through, write-behind. 무효화 시점(갱신 후 삭제), TTL은 안전망. 삭제와 재적재 사이의 경쟁.
- 답해 볼 것: ① 갱신 후 "삭제"가 "덮어쓰기"보다 나은 이유 ② 삭제 실패하면 ③ 읽기-갱신 경쟁으로 옛 값이 다시 들어가는 순서
- 읽을 곳: AWS 백서 "Database Caching Strategies Using Redis" · Facebook "Scaling Memcache at Facebook"(논문, lease 부분)
- 실습: 갱신·조회를 동시에 돌려 옛 값이 남는 비율 측정
- 손으로: cache-aside 조회·갱신 코드
- 끝: 볼트에 3줄

### CA-02 · 캐시 스탬피드
- 장면: 인기 키 TTL이 끝나는 순간 DB 부하 폭증.
- 이론: 동시 재계산 막기(락·single-flight), 확률적 조기 만료, 뒤에서 갱신, TTL 흩뿌리기.
- 답해 볼 것: ① 1,000 동시 요청 중 DB까지 가는 수 ② 락 대기자는 무엇을 받나 ③ TTL 지터의 효과
- 읽을 곳: "Optimal Probabilistic Cache Stampede Prevention"(VLDB 2015, XFetch)
- 실습: 만료 순간 동시 요청 → 방식별 DB 쿼리 수 예측 후 측정
- 손으로: single-flight 로직
- 끝: 볼트에 3줄

### CA-03 · 로컬 캐시 vs 원격 캐시
- 장면: Redis 왕복이 병목 / 서버마다 로컬 캐시 값이 다름.
- 이론: 2단 캐시, 로컬 캐시 무효화 전파(pub/sub), 메모리·GC 부담, 직렬화 비용.
- 답해 볼 것: ① 로컬 캐시가 맞는 데이터 ② 서버 간 불일치 허용 시간 ③ 직렬화 비용이 큰 경우
- 읽을 곳: Caffeine 위키
- 실습: 같은 조회를 DB / Redis / Caffeine으로 → p50·p99 예측 후 측정
- 손으로: 무효화 메시지 처리
- 끝: 볼트에 3줄

### CA-04 · Redis 자료구조와 함정
- 장면: `KEYS *` 한 번에 Redis가 멈춤 / 큰 키 삭제로 지연.
- 이론: 싱글 스레드 명령 처리, O(N) 명령, 큰 키, 만료 정책, 메모리 정책(eviction).
- 답해 볼 것: ① `KEYS` 대신 ② 큰 해시 하나 vs 작은 키 여러 개 ③ maxmemory에 닿으면
- 읽을 곳: Redis 문서 "Latency" · "Memory optimization"
- 실습: 큰 키 삭제 `DEL` vs `UNLINK` 지연 측정
- 손으로: 선착순·랭킹 하나를 자료구조로 설계
- 끝: 볼트에 3줄
