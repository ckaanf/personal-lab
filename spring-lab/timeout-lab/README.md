# timeout-lab

> **질문:** 클라이언트가 포기해도 서버 쿼리는 왜 계속 돌까? 그동안 무엇이 묶여 있을까?
> **역할 분담:** 무대(이 코드)는 AI, 예측·값 조정·실행·해석은 나.
> **이 문서만 보고 혼자 끝까지 할 수 있게** 써 두었다. 막혀도 AI에게 묻기 전에 아래 "막혔을 때"부터.

## 실험 목록

| # | 질문 | 이슈 | 상태 |
|---|---|---|---|
| 1 | 클라이언트가 끊은 뒤 쿼리는 언제 끝나고, 무엇이 묶여 있나 | #78 | 진행 전 |
| 2 | 쿼리 타임아웃을 클라이언트보다 짧게 / 길게 걸면? | (1 끝나면 생성) | |
| 3 | 풀 고갈과 DB 경합은 어떤 지표로 구별되나 | (2 끝나면 생성) | |

> 열린 실험 이슈는 한 번에 하나만.

---

## 0. 준비 (처음 한 번, 5분)

```bash
git fetch origin && git switch lab/timeout-lab
cd spring-lab/timeout-lab
```

- **Docker Desktop**이 켜져 있어야 한다. (`docker info`가 에러 없이 나오면 OK)
- **JDK 21**은 없어도 된다. 첫 실행 때 Gradle이 자동으로 내려받는다(시간이 좀 걸림).
- 처음 한 번 `./gradlew bootRun`을 돌려서 아래 로그가 보이면 준비 끝. `Ctrl+C`로 끈다.
  ```
  Started TimeoutLabApplication in ... seconds
  ```

---

## 1. 실험 1 진행 순서 (#78, 15분)

### ① 예측부터 쓴다 (2분) — **실행 전에**
GitHub에서 #78을 열고 `예측` 칸 두 개를 채운다.
- 클라이언트가 2초에 끊으면, 10초짜리 쿼리는 언제 끝날까?
- 그동안 Hikari active는 몇일까?

틀려도 된다. **틀린 곳이 배우는 곳이다.** 실행하고 나서 예측을 고치지 않는다.

### ② 터미널 3개를 띄운다

| 터미널 | 명령 | 역할 |
|---|---|---|
| A | `./gradlew bootRun` | 앱. `[slow] start / end` 로그를 본다 |
| B | (아래 ③의 curl) | 클라이언트 |
| C | (아래 관찰 명령) | 관찰 |

### ③ 실행 (1분)
터미널 B:
```bash
curl --max-time 2 "localhost:8080/slow?sec=10"; date +%T
```
2초 뒤 `curl: (28) Operation timed out`이 뜨고 시각이 찍힌다. **이 시각이 기준(0초)이다.**

### ④ 관찰 (바로 이어서, 1~2초 간격으로 10초 넘게)
터미널 C에서 두 개를 번갈아 친다.
```bash
curl -s localhost:8080/pool; echo
```
```bash
docker compose exec postgres psql -U lab -d lab -c \
  "SELECT pid, state, now() - query_start AS running, left(query, 40) AS query FROM pg_stat_activity WHERE query LIKE '%pg_sleep%' AND pid <> pg_backend_pid();"
```
그리고 터미널 A에서 `[slow] end elapsed=...ms` 로그가 **언제** 찍히는지 본다.

### ⑤ 기록 (5분)
#78의 `관찰` 표를 채운다. 해석은 쓰지 말고 **본 것만** 쓴다.
- 쿼리가 DB에서 사라진 시각은 기준에서 몇 초 뒤였나?
- 그동안 `active`는? `pending`은?
- `[slow] end` 로그는 찍혔나? 찍혔다면 언제? 그 뒤에 에러 로그가 있었나?

한 번 더 해 보고 싶으면 `sec=20`, `--max-time 1`처럼 값만 바꿔서 반복한다.

### ⑥ 해석 — 스스로 물어볼 것 (답은 여기 없다)
1. 예측과 무엇이 달랐나? 왜 달랐을까?
2. 클라이언트가 끊긴 걸 서버는 언제 알았나? (로그에서 근거를 찾는다)
3. 쿼리가 끝날 때까지 커넥션을 묶어 둔 건 누구인가?
4. 이 요청이 동시에 6개 들어오면(풀은 5개) 어떻게 될까? → 직접 해 봐도 된다
   ```bash
   for i in 1 2 3 4 5 6; do curl -s --max-time 2 "localhost:8080/slow?sec=10" & done; wait
   curl -s --max-time 5 localhost:8080/fast
   ```

### ⑦ 마무리 (3분)
- #78 `배운 점 (3줄)` 채우기 (목적 / 관찰 / 배운 점)
- 같은 3줄을 개인 볼트 `00_Workbench.md`에 + #78 링크
- 끝나면 `docker compose down`으로 DB를 끈다 (앱을 꺼도 DB 컨테이너는 남아 있다)
- 실험 2 이슈를 만들고 #78은 닫는다. (실험 템플릿: New issue → 🔬 실험)

---

## 2. 막혔을 때 (AI 없이 먼저)

| 증상 | 볼 것 |
|---|---|
| `Cannot connect to the Docker daemon` | Docker Desktop을 켠다 |
| `port 15432 already in use` | 다른 PostgreSQL이 떠 있다. `compose.yaml`의 `15432`를 `25432` 등으로 바꾼다 |
| `Port 8080 was already in use` | 다른 앱이 켜져 있다. `lsof -i :8080`으로 찾아서 끄거나, `./gradlew bootRun --args='--server.port=8081'` |
| `docker compose exec` 에서 `service "postgres" is not running` | 앱(터미널 A)이 먼저 떠 있어야 DB도 뜬다 |
| psql 결과가 항상 0줄 | `curl`을 친 **직후** 바로 조회했는지 확인. `sec`를 30으로 늘려서 여유를 둔다 |
| JDK 다운로드에서 멈춤 | 네트워크 확인. 이미 JDK 21이 있으면 `JAVA_HOME`을 그 경로로 |

그래도 막히면: 어디서 막혔는지 #78에 **한 줄** 남기고 멈춘다. 그것도 기록이다.

---

## 3. 다음 실험 미리보기 (1 끝난 뒤에)

**실험 2 — 타임아웃 층별로 걸어 보기.** `src/main/resources/application.yml`만 바꾼다.
```yaml
spring:
  jdbc:
    template:
      query-timeout: 1s      # 클라이언트(2초)보다 짧게 → 그다음엔 5s로 길게
```
DB 쪽 타임아웃(`statement_timeout`)도 걸어 보고 싶으면:
```yaml
spring:
  datasource:
    hikari:
      connection-init-sql: "SET statement_timeout = '1s'"
```
→ 각각 실험 1과 같은 방법으로 관찰하고 비교한다. 어느 쪽이 쿼리를 끊었는지, 앱 로그에 무엇이 찍히는지.

**실험 3 — 원인 구별하기.** 두 상황을 일부러 만들고 같은 지표를 본다.
- 풀 고갈: `/slow`를 6개 이상 동시에 → `/pool`의 `pending`, `/fast` 응답 시간
- DB 경합: `/heavy`(CPU를 쓰는 쿼리)를 여러 개 동시에 → `/fast` 응답 시간, `docker stats`의 DB CPU
```bash
for i in 1 2 3 4; do curl -s "localhost:8080/heavy?n=50000000" & done; wait
```
→ 두 상황에서 `pending`, `/fast` 응답 시간, DB CPU가 각각 어떻게 다른지 표로.

---

## 엔드포인트

| | 하는 일 |
|---|---|
| `GET /slow?sec=10` | `SELECT pg_sleep(sec)` — 기다리기만 하는 느린 쿼리 |
| `GET /heavy?n=30000000` | `SELECT count(*) FROM generate_series(1, n)` — CPU를 쓰는 느린 쿼리 |
| `GET /fast` | `SELECT 1` |
| `GET /pool` | Hikari active / idle / pending / total |

## 막힌 지점만 찾아볼 문서
- HikariCP 설정값: https://github.com/brettwooldridge/HikariCP#gear-configuration-knobs-baby
- PostgreSQL `pg_stat_activity`: https://www.postgresql.org/docs/16/monitoring-stats.html#MONITORING-PG-STAT-ACTIVITY-VIEW
- PostgreSQL `statement_timeout`: https://www.postgresql.org/docs/16/runtime-config-client.html
