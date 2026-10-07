# meal

개인 식단표를 여러 기기에서 같이 쓰기 위한 작은 구성. 식단표 HTML은 고치지 않는다.

```
맥 옵시디언 볼트 (재정/*식단표*.html)
  │  Mutagen 동기화
  ▼
서버 ~/vault/Vault/재정 ──(meal-publish.path가 감지)──▶ publish.py ──▶ /var/www/js/meal/YYYY-MM/
                                                                         + sync.js 한 줄 삽입
폰·PC 브라우저 ──Tailscale──▶ tailscale serve :8444 ──▶ Caddy :8082 ──┬─ 정적 파일
                                                                    └─ /meal/api/* ──▶ server.py :8083 ──▶ SQLite
```

## 동작

- **배포:** 볼트의 식단표를 고치면 몇 초 안에 `/meal/YYYY-MM/`에 반영된다. 월은 파일 안의 저장 키 `meal-YYYY-MM`에서 읽는다.
- **`/meal/`:** 이번 달 식단표로 바로 이동한다. 목록은 `/meal/#list`.
- **동기화:** 식단표는 기록을 `localStorage`의 `meal-...` 키에 JSON 하나로 저장한다. `sync.js`가 그 앞에서
    - 처음 읽을 때 서버 기록을 받아 합치고,
    - 저장할 때 바뀐 "섹션/키"만 서버로 보내고 (실패하면 대기열에 두고 다시 보냄),
    - 탭으로 돌아올 때 다른 기기 변경을 받아 새로 고친다.
- **충돌:** 서버는 키마다 가장 늦게 바뀐 값만 남긴다. 다른 끼니를 동시에 고치면 둘 다 남는다.
- **처음 연결:** 그 기기에만 있던 기록은 서버로 올라간다. 서버에 같은 키가 있으면 서버 값이 이긴다.
- **접근 제어:** Tailscale 전용이라 API에 별도 인증이 없다. 인터넷에 열면 안 된다.

## 파일

| 파일 | 역할 |
| --- | --- |
| `server.py` | 기록 저장 API (`http.server` + `sqlite3`), 127.0.0.1:8083 |
| `sync.js` | 브라우저 동기화. 식단표 페이지에 `<script src="/meal/sync.js">`로 삽입됨 |
| `publish.py` | 볼트 원본 → `/var/www/js/meal` 배포, 월 목록 페이지 생성 |
| `backup.py` | DB 백업 (`~/backups/meal`, 30개 보관) |
| `test_sync.js` | 브라우저 두 대를 흉내 낸 동기화 시험 (Node.js) |
| `systemd/` | `meal-api.service`, `meal-publish.path/.service`, `meal-backup.timer/.service` |

데이터는 `~/meal-data/meal.db`(SQLite)에 있고 저장소에는 넣지 않는다.

## 설치

```bash
sudo cp systemd/meal-* /etc/systemd/system/
sudo mkdir -p /var/www/js/meal && sudo chown -R ubuntu:ubuntu /var/www/js/meal
sudo systemctl daemon-reload
sudo systemctl enable --now meal-api.service meal-publish.path meal-backup.timer
sudo systemctl start meal-publish.service
```

## 시험

```bash
MEAL_DB=/tmp/t.db MEAL_PORT=18083 python3 server.py &
node test_sync.js sync.js
```

## 새 달 추가

볼트 `재정/`에 새 식단표 HTML(저장 키 `meal-YYYY-MM-...`)을 넣으면 자동으로 배포되고 목록에 추가된다. 지난 달 기록은 그대로 남는다.
