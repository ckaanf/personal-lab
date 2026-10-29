# 백엔드 도서관

> 바텀업으로 자료를 모으는 대신, **서가에서 필요한 칸을 꺼내 보는** 곳 (2026-10-29 시작).
> 칸 하나 = 실무 장면 → 이론 → 실습(예측하고 재기) → 내 말로 설명. AI가 코드를 빨리 만들어 주니, 실습의 중심은 "만들기"가 아니라 "예측과 측정"이다.

## 어떻게 쓰나
1. 아래 서가에서 지금 부딪힌 것과 가까운 **카드**를 하나 고른다 (한 번에 하나).
2. 카드의 **이론 질문 3개**에 먼저 답해 본다 (모르면 "읽을 곳"부터).
3. **실습**: 숫자를 먼저 예측하고, AI가 세운 무대에서 잰다. "손으로" 칸의 코드는 AI 없이 쓴다.
4. 볼트 `Notes/실험기록/`에 내 말로 3줄을 남긴다 (아래 "볼트와 잇기").
5. 깊게 하고 싶어지면 그 서가의 **실험실(lab)** 로 간다.

## 볼트와 잇기
| 어디 | 무엇 | 누가 쓰나 |
|---|---|---|
| 이 레포 `library/` | 서가·카드·실험실 커리큘럼 (언제든 다시 만들 수 있는 것) | AI |
| 이 레포의 각 lab 폴더 | 실습 코드·측정 결과 | 나 + AI |
| 볼트 `Notes/실험기록/` | 예측 / 관찰 / 내 말로 3줄, 내가 내린 결정 | **나만** |

- 볼트 노트 맨 위에 `library: DB-02` 처럼 **카드 ID**를 적는다. 카드 ID는 바뀌지 않는다 (카드를 고쳐도 번호 유지, 없앨 때는 "버림"으로 남김).
- 레포는 공개라 볼트로 링크하지 않는다. 볼트 → 레포 방향으로만 잇는다: `https://github.com/ckaanf/personal-lab/blob/main/library/shelves/<파일>#<카드ID>`
- 회사 정보·개인 재정·구매 내역은 이 레포에 넣지 않는다 (공개 레포).

## 서가
| 코드 | 서가 | 카드 | 깊게(lab) |
|---|---|---|---|
| TX | [정합성·트랜잭션](shelves/TX-consistency.md) | 6 | [결제 정합성 실험실](../spring-lab/payment-consistency/README.md) |
| DB | [DB 성능](shelves/DB-performance.md) | 6 | [DB 성능 실험실](labs/db-performance/README.md) |
| CC | [동시성 (JVM)](shelves/CC-concurrency.md) | 5 | — |
| DS | [분산 시스템 기초](shelves/DS-distributed.md) | 5 | — |
| MQ | [메시징·이벤트](shelves/MQ-messaging.md) | 5 | — |
| CA | [캐시](shelves/CA-cache.md) | 4 | — |
| PF | [성능·부하](shelves/PF-performance.md) | 5 | — |
| OP | [운영·관측·장애](shelves/OP-operations.md) | 7 | [운영·복구 실험실](labs/ops-recovery/README.md) |
| NW | [네트워크·프로토콜](shelves/NW-network.md) | 5 | — |
| MD | [미디어 지연](shelves/MD-media.md) | 5 | [미디어 지연 실험실](labs/media-latency/README.md) |
| API | [API 설계·호환성](shelves/API-design.md) | 4 | — |
| TS | [테스트 전략](shelves/TS-testing.md) | 4 | — |
| SEC | [보안 기초](shelves/SEC-security.md) | 4 | — |
| AI | [AI 하네스](shelves/AI-harness.md) | 5 | [AI 하네스 실험실](labs/ai-harness/README.md) |

## 카드 모양
```
### DB-02 · 제목
- 장면: 이 주제가 실제로 터지는 상황
- 이론: 핵심 몇 줄
- 답해 볼 것: 질문 3개
- 읽을 곳: 책 장 · 공식 문서
- 실습: 예측 → 측정 (무대는 AI가 세워도 된다)
- 손으로: AI 없이 쓰는 핵심 코드/쿼리 하나
- 끝: 내 말로 세 문장을 볼트에
```

## 자주 쓰는 책 (약칭)
- **DDIA** — 『데이터 중심 애플리케이션 설계』(Kleppmann)
- **RealMySQL** — 『Real MySQL 8.0』
- **JCIP** — 『Java Concurrency in Practice』
- **SRE** — 『Site Reliability Engineering』(Google, 웹 무료)
- **HPBN** — 『High Performance Browser Networking』(Grigorik, 웹 무료)
