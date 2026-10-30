/* mk1 블록 예시 — ex('카드ID', '이름', html). examples.html(하나씩)과 kit.html(한 장)이 같이 읽는다.
   tools/build.py 가 ex('...') 를 세어 룩북에 "예시 있음"을 붙인다 */
(function () {
  'use strict';
  var I = window.MK1.icon, list = [];
  function ex(id, name, html, opt) { list.push({ id: id, name: name, html: html, opt: opt || {} }); }
  function tok() { return (window.DS_TOKENS || { groups: [] }).groups; }
  function sw(names) {
    var all = {}; tok().forEach(function (g) { g.tokens.forEach(function (t) { all[t.name] = t; }); });
    return '<div class="ex-sw">' + names.map(function (n) {
      var t = all[n] || { label: n };
      return '<div><i style="background:var(--' + n + ')"></i><b>' + t.label + '</b><code>--' + n + '</code></div>';
    }).join('') + '</div>';
  }

  /* ── 토큰 ── */
  ex('T-01', '색은 역할로', function () {
    return '<h4>글자</h4>' + sw(['c-ink', 'c-ink-2', 'c-ink-3']) + '<h4>면 · 선</h4>' + sw(['c-bg', 'c-surface', 'c-sunken', 'c-neutral-soft', 'c-line', 'c-line-strong']) +
      '<h4>주 색</h4>' + sw(['c-primary', 'c-primary-soft']) + '<h4>쪽</h4>' + sw(['c-side-them', 'c-side-me']);
  });
  ex('T-02', '뜻 색 = 색 · 면 · 선', function () {
    var m = [['ok', '완료'], ['hold', '보류'], ['wait', '기다림'], ['danger', '내 할 일'], ['info', '진행'], ['accent', '특별 기능']];
    return '<div class="ex-row">' + m.map(function (x) { return '<span class="st ' + x[0] + '">' + x[1] + '</span>'; }).join('') + '<span class="st">끝 · 제외</span></div>' +
      '<div class="ex-sw soft">' + m.map(function (x) { return '<div><i style="background:var(--c-' + x[0] + '-soft);border-color:var(--c-' + x[0] + '-line)"></i><b style="color:var(--c-' + x[0] + ')">' + x[1] + '</b><code>-soft · -line</code></div>'; }).join('') + '</div>' +
      '<p class="muted">뜻 색은 점과 글자에만. 면은 눈을 끌어야 할 때만.</p>';
  });
  ex('T-03', '글자 5단계', '<div class="ex-type">' +
    '<div><code>fs-xl</code><span style="font-size:var(--fs-xl);font-weight:var(--fw-title)">요청 목록</span></div>' +
    '<div><code>fs-lg</code><span style="font-size:var(--fs-lg);font-weight:var(--fw-title)">확인할 것</span></div>' +
    '<div><code>fs-md</code><span>저장 버튼을 목록 위로 옮겨 주세요</span></div>' +
    '<div><code>fs-sm</code><span style="font-size:var(--fs-sm);color:var(--c-ink-2)">진행 중 · 2일째</span></div>' +
    '<div><code>fs-xs</code><span style="font-size:var(--fs-xs);color:var(--c-ink-3)">김하나 · 10-30 14:02</span></div></div>' +
    '<p class="muted">위 "보기"를 편하게로 바꾸면 한 단계씩 커진다(L-06).</p>');
  ex('T-04', '둥글기 3단계', '<div class="ex-row" style="gap:24px">' +
    '<div class="ex-r"><i style="border-radius:var(--r-control)"></i><span>6 · 버튼 · 입력 · 꼬리표</span></div>' +
    '<div class="ex-r"><i style="border-radius:var(--r-box)"></i><span>8 · 카드 · 상자 · 창</span></div>' +
    '<div class="ex-r"><i style="border-radius:var(--r-round);width:44px"></i><span>동그라미 · 점 · 탭 줄</span></div></div>');
  ex('T-05', '간격 · 높이', '<div class="ex-sp">' + [1, 2, 3, 4, 5, 6].map(function (n) { return '<div><i style="width:var(--sp-' + n + ')"></i><code>sp-' + n + '</code></div>'; }).join('') + '</div>' +
    '<div class="ex-row" style="margin-top:16px"><button class="btn">32 기본</button><button class="btn sm">28 작게</button><button class="btn" style="height:var(--h-touch)">44 폰</button></div>');
  ex('T-06', '밝은 / 어두운 모드', '<div class="ex-modes"><div data-mode="light" class="ex-mode"><b>밝게</b><span class="st mine">답하기</span><span class="st ok">완료</span><button class="btn primary sm">보내기</button></div>' +
    '<div data-mode="dark" class="ex-mode"><b>어둡게</b><span class="st mine">답하기</span><span class="st ok">완료</span><button class="btn primary sm">보내기</button></div></div>' +
    '<p class="muted">같은 토큰 이름에 모드별 값. 시스템 설정을 따르고, 사람이 고르면 기억한다.</p>');

  /* ── 부품 ── */
  ex('C-01', '버튼', '<div class="ex-row"><button class="btn primary">보내기</button><button class="btn">저장</button><button class="btn quiet">닫기</button><button class="btn danger">지우기</button><button class="btn primary" disabled>담기</button></div>' +
    '<div class="ex-row"><button class="btn sm primary">검토 요청</button><button class="btn sm">다시 열기</button><button class="btn sm quiet">더 보기</button></div>' +
    '<div class="ex-row"><button class="btn primary" data-once>두 번 눌러 보기</button><span class="muted">보내는 중에는 막힌다(F-06)</span></div>');
  ex('C-02', '고르기', '<div class="ex-row"><span class="seg" role="group" aria-label="회신 종류"><button aria-pressed="true">완료</button><button aria-pressed="false">보류</button><button aria-pressed="false">되묻기</button></span></div>' +
    '<div class="ex-row"><span class="seg"><button aria-pressed="true">전체 <span class="muted">24</span></button><button aria-pressed="false">내 할 일 <span class="muted">3</span></button><button aria-pressed="false">기다림 <span class="muted">8</span></button></span><span class="muted">0건 칩은 숨긴다</span></div>');
  ex('C-03', '상태 표시', '<div class="ex-row"><span class="st mine">답하기</span><span class="st wait">상대 확인 중</span><span class="st info">진행 중</span><span class="st hold">보류</span><span class="st ok">완료</span><span class="st">제외</span></div>' +
    '<div class="ex-row" style="filter:grayscale(1)"><span class="st mine">답하기</span><span class="st wait">상대 확인 중</span><span class="st ok">완료</span><span class="muted">흑백에서도 글자로 읽힌다</span></div>');
  ex('C-04', '이름표', '<div class="ex-row"><span class="tag">내부만</span><span class="tag">AI와 함께</span><span class="tag">시안</span><span class="tag">2판</span></div><p class="muted">상태가 아닌 종류·출처. 뜻 색을 쓰지 않는다.</p>');
  ex('C-05', '입력 칸', '<div class="ex-grid">' +
    '<label class="field"><span class="lbl">검토할 화면 주소</span><input class="input" placeholder="https://"><span class="help">로그인 없이 열리는 주소</span></label>' +
    '<label class="field"><span class="lbl">검토</span><select class="select"><option>검토 받음</option><option>검토 생략</option></select></label>' +
    '<label class="field err"><span class="lbl">이름</span><input class="input" value="가"><span class="help">두 글자 이상 적어 주세요</span></label>' +
    '<label class="field"><span class="lbl">번호</span><input class="input" value="R-0012" disabled></label>' +
    '<label class="field" style="grid-column:1/-1"><span class="lbl">볼 것</span><textarea class="textarea">검색 칸 세 개와 목록 위 건수 줄</textarea></label></div>');
  ex('C-06', '주고받기 입력 칸', '<div class="compose" data-audience="상대 회사"><textarea placeholder="적을 내용" aria-label="적을 내용"></textarea><div class="warn" hidden></div>' +
    '<div class="bar"><span class="seg"><button aria-pressed="true" data-aud="internal">내부 메모</button><button aria-pressed="false" data-aud="public">상대에게 답글</button></span><span class="sp"></span><span class="muted"><kbd>⌘</kbd> <kbd>Enter</kbd></span><button class="btn sm primary" data-send data-once>메모 남기기</button></div></div>' +
    '<p class="muted">열 때마다 내부 메모가 기본이고 점선 칸이다. 답글을 고르면 경고 줄과 버튼 이름이 바뀐다.</p>');
  ex('C-07', '주고받은 글', '<div class="thread" style="max-width:560px">' +
    '<div class="msg"><div class="who"><span class="face">상</span>상대 담당 · 10-29 17:10</div><div class="txt">수정 버튼을 맨 앞으로 옮겨 주세요</div></div>' +
    '<div class="msg me"><div class="who"><span class="st wait">되묻기</span> 나 · 10-30 09:36</div><div class="txt">체크박스 오른쪽인가요, 번호 칸 앞인가요?</div></div>' +
    '<div class="msg"><div class="who"><span class="face">상</span>상대 담당 · 10-30 13:02</div><div class="txt">체크박스 오른쪽 첫 칸이요</div></div>' +
    '<div class="msg me draft"><div class="who">보내기 전</div><div class="txt">반영하겠습니다</div></div>' +
    '<div class="read">읽음 · 13:05</div></div>');
  ex('C-08', '카드', '<div style="display:grid;gap:12px;max-width:640px">' +
    '<section class="card" data-fold="ex-c08a"><div class="card-h">화면 의견 <span class="count">3</span><span class="sp"></span><span class="st mine">회신 2</span>' + I('chevron', 'fold') + '</div><div class="card-b">' +
    '<div class="row"><span class="no">1</span><div class="body">저장 버튼을 목록 위로<div class="meta">상대 담당 · 10-29</div></div><button class="btn sm quiet">수정하기</button></div>' +
    '<div class="row"><span class="no">2</span><div class="body">검색 칸 기본값을 이번 달로<div class="meta">상대 담당 · 10-29</div></div><button class="btn sm quiet">수정하기</button></div></div></section>' +
    '<section class="card folded" data-fold="ex-c08b"><div class="card-h">주고받은 판 <span class="count">2</span><span class="sp"></span>' + I('chevron', 'fold') + '</div><div class="card-b">1판 · 2판</div></section></div>' +
    '<p class="muted">머리를 누르면 접힌다. 접은 상태는 카드 종류별로 기억한다.</p>');
  ex('C-09', '표', '<div class="table-wrap"><table class="table"><thead><tr><th>번호</th><th>내용</th><th class="opt">분류</th><th>단계</th><th class="num">토큰</th><th>다음</th></tr></thead><tbody>' +
    '<tr><td class="id">R-0006</td><td>사용자 관리 검색 칸·건수·정렬</td><td class="opt">화면</td><td><span class="st info">검토 중</span></td><td class="num">48만</td><td class="muted">상대 확인 3일째</td></tr>' +
    '<tr><td class="id">R-0027</td><td>권한 메뉴 정리</td><td class="opt">설정</td><td><span class="st">개발 완료</span></td><td class="num">1.2억</td><td><span class="st mine">검토 요청</span></td></tr>' +
    '<tr><td class="id">R-0031</td><td>결제 정보 총액 복구</td><td class="opt">오류</td><td><span class="st ok">반영 완료</span></td><td class="num"></td><td class="muted">끝</td></tr>' +
    '</tbody></table></div><p class="muted">0은 비우고 큰 수는 만·억. 중간 폭에서는 분류 칸을 숨긴다.</p>');
  ex('C-10', '창', '<div class="modal" role="dialog" aria-labelledby="ex-m-h" style="margin:0 auto"><h3 id="ex-m-h">보류</h3><p class="note" style="margin:0">보류하는 이유를 적어 주세요. 상대에게도 보입니다.</p>' +
    '<label class="field"><span class="lbl">이유</span><textarea class="textarea"></textarea></label><div class="actions"><button class="btn quiet">닫기</button><button class="btn primary">보류</button></div></div>');
  ex('C-11', '새 소식', '<div class="news" style="margin:0 auto"><div class="news-h">새 소식<button class="btn sm quiet">모두 읽음</button></div><ul>' +
    '<li><span class="dot"></span><div class="body"><div class="kind">회신</div><div class="text">체크박스 오른쪽 첫 칸이요. 그리고 아주 긴 글이 들어와도 두 줄에서 줄여 자르고 옆으로 밀리지 않습니다</div><div class="time">13:02</div></div></li>' +
    '<li><span class="dot"></span><div class="body"><div class="kind">새 요청 3건</div><div class="text">R-0040 · R-0041 · R-0042 외 0건</div><div class="time">11:20</div></div></li>' +
    '<li class="read"><span class="dot"></span><div class="body"><div class="kind">검토 완료</div><div class="text">R-0031 결제 정보 총액 복구</div><div class="time">어제</div></div></li></ul></div>');

  /* ── 틀 ── */
  function topbar(where, opt) {
    opt = opt || {};
    return '<header class="topbar">' +
      '<button class="icon-btn" data-act="nav" aria-label="메뉴">' + I('menu') + '</button>' +
      '<div class="where">' + where + '</div>' +
      '<label class="find">' + I('search') + '<input placeholder="번호나 이름으로 찾기" aria-label="찾기"><kbd>/</kbd></label>' +
      '<button class="icon-btn find-btn" data-act="find" aria-label="찾기">' + I('search') + '</button>' +
      (opt.noPlus ? '' : '<button class="icon-btn" aria-label="새로 만들기" title="새로 만들기">' + I('plus') + '</button>') +
      '<button class="icon-btn" aria-label="새 소식 2건" title="새 소식">' + I('bell') + '<span class="dot"></span></button>' +
      '<button class="me" data-act="pop" data-target="' + (opt.pop || 'ex-me') + '" aria-label="내 계정">하</button>' +
      '<div class="menu-pop" id="' + (opt.pop || 'ex-me') + '" hidden><div class="who"><b>김하나</b><small>담당자</small></div><button>' + I('user') + '설정</button><button>' + I('tool') + '비밀번호 바꾸기</button><button>' + I('alert') + '나가기</button></div>' +
      '</header>';
  }
  function navGroups(cur) {
    var G = [
      { k: 'work', h: '일', items: [['home', '홈', '홈', '', 0], ['inbox', '받은 요청', '요청', 3], ['chat', '논의', '논의', 12], ['check', '검토', '검토']] },
      { k: 'rec', h: '기록', dot: true, items: [['layers', '지난 요청', '지난'], ['grid', '통계', '통계']] },
      { k: 'adm', h: '관리', items: [['user', '계정', '계정'], ['tool', '설정', '설정']] }
    ];
    return G.map(function (g) {
      return '<div class="grp" data-grp="ex-' + g.k + '"><button class="grp-h">' + g.h + (g.dot ? '<span class="gdot has"></span>' : '') + '<span class="cv">▾</span></button><div class="items">' +
        g.items.map(function (it) {
          var on = it[1] === cur;
          return '<a class="nav-it" href="#"' + (on ? ' aria-current="page"' : '') + '>' + I(it[0]) + '<span class="full">' + it[1] + '</span><span class="short">' + it[2] + '</span>' +
            (typeof it[3] === 'number' && it[3] ? '<span class="badge">' + it[3] + '</span>' : '') + '</a>';
        }).join('') + '</div></div>';
    }).join('');
  }
  ex('L-01', '윗줄', topbar('<b>업무 포털</b><span class="hide-phone">›</span><span class="hide-phone">받은 요청</span>') +
    '<p class="muted" style="padding:0 4px">☰ 메뉴 · 위치 · <kbd>/</kbd> 찾기 · + · 새 소식(빨간 점) · 내 동그라미(누르면 계정 메뉴). 폭이 좁으면 찾기가 돋보기로 바뀐다.</p>', { flush: true });
  ex('L-02', '왼쪽 메뉴', '<div class="ex-navs"><div><p class="muted">펼침</p><nav class="sidenav ex-static">' + navGroups('받은 요청') + '</nav></div>' +
    '<div class="rail"><p class="muted">접힘 — 아이콘 밑에 짧은 이름</p><nav class="sidenav ex-static">' + navGroups('받은 요청') + '</nav></div></div>');
  ex('L-03', '메뉴 묶음 접기', '<nav class="sidenav ex-static" style="height:auto">' + navGroups('논의').replace('class="grp" data-grp="ex-rec"', 'class="grp closed" data-grp="ex-rec-demo"') + '</nav>' +
    '<p class="muted">묶음 머리를 눌러 접는다. "기록"은 접혀 있고 안에 새 것이 있어 점이 보인다. 지금 화면이 든 묶음은 늘 펼친다.</p>');
  function phone(cur, open) {
    var cards = ['R-0042 저장 버튼 위치', 'R-0041 검색 기본값', 'R-0040 권한 메뉴', 'R-0039 엑셀 내려받기', 'R-0038 알림 문구', 'R-0037 결제 총액'].map(function (t, i) {
      var st = [['mine', '답하기'], ['wait', '상대 확인 중'], ['info', '진행 중'], ['ok', '완료'], ['hold', '보류'], ['ok', '완료']][i];
      return '<div class="card"><div class="card-b" style="display:grid;gap:4px"><span class="mono muted">' + t.slice(0, 6) + '</span><b>' + t.slice(7) + '</b><span class="st ' + st[0] + '">' + st[1] + '</span></div></div>';
    }).join('');
    var tabs = [['home', '홈'], ['inbox', '요청', 1], ['chat', '논의'], ['check', '검토'], ['menu', '더보기']];
    return '<div class="phone-frame' + (open ? ' drawer-open' : '') + '">' + topbar('<b>받은 요청</b>', { noPlus: true, pop: 'ex-me-' + (open ? 'b' : 'a') }) +
      '<div class="scroll">' + cards + '</div>' +
      '<nav class="tabbar" aria-label="아래 탭">' + tabs.map(function (t) { return '<a class="tab" href="#"' + (t[1] === cur ? ' aria-current="page"' : '') + (t[0] === 'menu' ? ' data-act="nav"' : '') + '>' + I(t[0]) + '<span>' + t[1] + '</span>' + (t[2] ? '<span class="dot"></span>' : '') + '</a>'; }).join('') + '</nav>' +
      '<div class="drawer-bg"></div><nav class="sidenav">' + navGroups('받은 요청') + '</nav></div>';
  }
  ex('L-04', '폰 유리 탭 줄 + 서랍', '<div class="ex-phones"><div>' + phone('요청') + '</div><div>' + phone('요청', true) + '</div></div>' +
    '<p class="muted">탭 이름은 늘 보인다. 지금 탭은 유리 방울. 위 "투명도 줄이기"를 켜면 불투명해진다. ☰ 또는 더보기로 서랍.</p>', { phone: true });
  ex('L-05', '상세 화면 틀', '<div class="ex-detail"><div class="detail-head"><button class="btn sm quiet">목록</button><button class="btn sm quiet" aria-label="이전">◀</button><span class="muted">6 / 82</span><button class="btn sm quiet" aria-label="다음">▶</button>' +
    '<span class="no">R-0042</span><h1>저장 버튼 위치</h1><span class="st info">진행 중</span><span class="sp"></span><span class="seg"><button aria-pressed="true">촘촘하게</button><button aria-pressed="false">편하게</button></span><button class="btn sm" data-act="info">' + I('info') + '정보 칸</button></div>' +
    '<div class="detail-grid"><section class="card"><div class="steps"><span class="done">받음</span><span class="sep">›</span><span class="done">논의</span><span class="sep">›</span><span class="on">진행</span><span class="sep">›</span><span>검토</span><span class="sep">›</span><span>반영</span></div></section>' +
    '<section class="card"><div class="todo"><span class="say">개발을 마쳤으면 검토를 요청하세요</span><button class="btn primary">검토 요청</button><button class="btn quiet">더 보기</button></div></section>' +
    '<section class="card" data-fold="ex-l05a"><div class="card-h">요청 내용<span class="sp"></span>' + I('chevron', 'fold') + '</div><div class="card-b">저장 버튼을 목록 위로 옮겨 주세요. 아래까지 내려야 해서 불편합니다.</div></section>' +
    '<section class="card folded" data-fold="ex-l05b"><div class="card-h">논의 · 끝남 <span class="count">회신 4</span><span class="sp"></span>' + I('chevron', 'fold') + '</div><div class="card-b">…</div></section></div></div>' +
    '<aside class="info-panel" aria-label="정보 칸"><div class="card"><div class="card-h">정보<span class="sp"></span><button class="btn sm quiet" data-act="info">닫기</button></div><div class="card-b" style="display:grid;gap:8px;font-size:var(--fs-sm)">' +
    '<div><span class="muted">맡은 사람</span><br>김하나</div><div><span class="muted">받은 날</span><br>10-29</div><div><span class="muted">긴급도</span><br>보통</div></div></div></aside>' +
    '<p class="muted">머리 줄 → 단계 줄 → 지금 할 일 하나 → 본문 카드. 정보 칸은 오른쪽에서 열린다(<kbd>]</kbd>).</p>');
  ex('L-06', '보기 설정', '<div class="ex-modes"><div class="ex-mode" data-density="compact" style="align-items:stretch;flex-direction:column"><b>촘촘하게</b><div class="thread"><div class="msg"><div class="txt">버튼을 앞으로 옮겨 주세요</div></div></div></div>' +
    '<div class="ex-mode" data-density="comfy" style="align-items:stretch;flex-direction:column"><b>편하게</b><div class="thread"><div class="msg"><div class="txt">버튼을 앞으로 옮겨 주세요</div></div></div></div></div>' +
    '<p class="muted">바깥 사용자는 편하게, 매일 많이 보는 내부 사용자는 촘촘하게가 기본. 바꾸면 기억한다.</p>');

  /* ── 동작 ── */
  ex('F-02', '초안과 보내기', '<div class="ex-draft"><span class="st wait">보내지 않은 내용 있음</span><span class="muted">초안 저장 14:02</span><span class="sp"></span><button class="btn sm">저장</button><button class="btn sm primary" data-once>보내기</button></div>' +
    '<p class="muted">몇 초 쉬면 나만 보는 초안으로 저장한다. 상대에게는 보낸 것만 보인다.</p>');
  ex('F-03', '새 소식과 읽음', '<div class="thread" style="max-width:520px"><div class="msg me"><div class="who">나 · 10:20</div><div class="txt">수정한 화면 확인 부탁드립니다</div></div><div class="read">읽음 · 10:41</div>' +
    '<div class="msg"><div class="who"><span class="face">상</span>상대 담당 · 10:44 <span class="tag">새로</span></div><div class="txt">확인했습니다</div></div></div>');
  ex('F-06', '되돌릴 수 없는 일은 한 번 더', '<div class="ex-row"><button class="btn primary" data-once>보내기</button><span class="muted">빠르게 두 번 눌러도 한 번만 간다</span></div>');

  /* ── 도구 ── */
  ex('X-01', '조종 패널', '<div class="ex-row"><a class="btn primary" href="kit.html" target="_top">부품 한 장 보기에서 열기</a><span class="muted">오른쪽 패널에서 토큰을 움직여 보고 tokens.json 으로 복사한다</span></div>');
  ex('X-02', '부품 한 장 보기', '<div class="ex-row"><a class="btn primary" href="kit.html" target="_top">부품 한 장 보기 열기</a><span class="muted">모든 예시를 한 페이지에, 조종 패널과 함께</span></div>');

  window.MK1_EX = list;
})();
