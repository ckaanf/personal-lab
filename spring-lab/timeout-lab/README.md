# timeout-lab

> 질문: 클라이언트가 포기해도 서버 쿼리는 왜 계속 돌까? 그동안 무엇이 묶여 있을까?
> 역할 분담: 무대(이 코드)는 AI, 예측·값 조정·실행·해석은 나.

## 실행

```bash
./gradlew bootRun          # compose.yaml의 PostgreSQL(15432)도 같이 뜬다
```

| 엔드포인트 | 하는 일 |
|---|---|
| `GET /slow?sec=10` | `SELECT pg_sleep(sec)` — 일부러 느린 쿼리 |
| `GET /fast` | `SELECT 1` |
| `GET /pool` | Hikari active / idle / pending / total |

## 관찰 도구

```bash
# 클라이언트: 2초에 포기
curl --max-time 2 "localhost:8080/slow?sec=10"

# 풀 상태
curl -s localhost:8080/pool

# DB에서 돌고 있는 쿼리
docker compose exec postgres psql -U lab -d lab -c \
  "SELECT pid, state, now() - query_start AS running, query FROM pg_stat_activity WHERE query LIKE '%pg_sleep%' AND pid <> pg_backend_pid();"
```

## 실험 기록

실험별 예측·관찰·배운 점은 GitHub 이슈에 남긴다.

- 실험 1. 고아 쿼리 관찰 — #78
