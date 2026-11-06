/* 룩북 — catalog.js(DS_CATALOG)를 읽어 시스템 · 블록 목록 · 블록 상세를 그린다.
   주소: #/mk1 (블록 목록) · #/mk1?c=C (묶음, D 는 사전에만 있음) · #/mk1/C-03 (상세). 부르는 이름은 늘 "시스템_카드ID".
   담은 것(tray)은 이 브라우저에 기억하고, 위 → 아래 순서로 부탁 문장 한 줄을 만든다 */
(function () {
  'use strict';
  var CAT = window.DS_CATALOG || { systems: [] }, I = window.MK1.icon, P = window.MK1.prefs;
  var $ = function (s) { return document.querySelector(s); };
  var state = { sys: null, cat: '*', q: '', id: null, view: { mode: null, w: 'desk', density: 'compact' } };

  function esc(s) { return String(s == null ? '' : s).replace(/[&<>"]/g, function (c) { return { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;' }[c]; }); }
  function sysById(id) { return CAT.systems.filter(function (s) { return s.id === id; })[0]; }
  function cardById(sys, id) { return sys.cards.filter(function (c) { return c.id === id; })[0]; }
  function field(c, k) { var f = c.fields.filter(function (x) { return x[0] === k; })[0]; return f ? f[1] : ''; }
  function home(s) { return s.home || s.id; }
  function pre(s, j) { return s.base ? (j || '&') + 'preset=' + s.id : ''; }
  function ask(sys, c) { return sys.id + '_' + c.id + ' 구현해줘'; }
  // 카드 글 → 짧은 HTML: `코드`, **굵게**, "- " 줄은 목록, 카드 ID 는 링크
  function md(text, sys) {
    var lines = esc(text).split('\n'), out = [], list = [];
    function inline(s) {
      return s.replace(/`([^`]+)`/g, '<code>$1</code>').replace(/\*\*([^*]+)\*\*/g, '<b>$1</b>')
        .replace(/\b([PWTCGLFXQ]-\d{2})\b/g, function (m) { return cardById(sys, m) ? '<a href="#/' + sys.id + '/' + m + '">' + m + '</a>' : m; });
    }
    function flush() { if (list.length) { out.push('<ul>' + list.map(function (l) { return '<li>' + inline(l) + '</li>'; }).join('') + '</ul>'); list = []; } }
    lines.forEach(function (l) { if (/^- /.test(l)) list.push(l.slice(2)); else { flush(); if (l.trim()) out.push(inline(l)); } });
    flush();
    return out.join('<br>');
  }

  var TKEY = 'ds-tray', tray = { frame: 'phone', title: '', items: [] };
  try { Object.assign(tray, JSON.parse(localStorage.getItem(TKEY) || '{}')); } catch (e) { /* 기억 못 해도 담기는 된다 */ }
  function saveTray() { try { localStorage.setItem(TKEY, JSON.stringify(tray)); } catch (e) { /* 위와 같음 */ } }
  function trayKey(kind, id) { return kind + ':' + id; }
  function inTray(kind, id) { return tray.items.some(function (x) { return trayKey(x.kind, x.id) === trayKey(kind, id); }); }
  function toggleTray(kind, id, name) {
    if (inTray(kind, id)) tray.items = tray.items.filter(function (x) { return trayKey(x.kind, x.id) !== trayKey(kind, id); });
    else { tray.items.push({ kind: kind, id: id, name: name }); document.body.classList.add('lb-tray-open'); }
    saveTray(); drawTray(); drawTopCount(); drawTabs();
    document.querySelectorAll('[data-pick="' + trayKey(kind, id) + '"]').forEach(function (b) { pickBtn(b, inTray(kind, id)); });
  }
  function pickBtn(b, on) { b.setAttribute('aria-pressed', String(on)); b.textContent = on ? '✓ 담음' : '담기'; b.closest('.blk') && b.closest('.blk').classList.toggle('on', on); }
  // 부탁 문장 — 다른 세션이 카드 파일만 보고 구현할 수 있게 ID 로만 쓴다
  function trayAsk() {
    var s = state.sys, miss = tray.items.some(function (x) { return x.kind === 'dict'; });
    var head = s.id + (s.base ? ' 프리셋' : '') + ' · ' + (tray.frame === 'phone' ? '폰' : '데스크톱') + ' 화면' + (tray.title ? ' "' + tray.title + '"' : '');
    var seq = tray.items.map(function (x) { return x.kind === 'card' ? s.id + '_' + x.id : '[' + x.name + ': 카드에 없음]'; }).join(' → ');
    return head + '\n위에서 아래로: ' + seq + '\n구현해줘.' + (miss ? ' 카드에 없는 것은 만들기 전에 먼저 물어봐.' : '');
  }
  function drawTray() {
    var box = $('#tray'); if (!box) return;
    var n = tray.items.length;
    box.innerHTML = '<div class="lb-tray-h"><b>담은 것</b><span class="ds-muted">' + n + ' · 위에서 아래로</span><span class="sp"></span><button class="ds-btn sm quiet" data-tray="close" aria-label="닫기">닫기</button></div>' +
      (n ? '<div class="lb-tray-opts"><span>화면</span><span class="ds-seg"><button data-tray="frame" data-val="phone" aria-pressed="' + (tray.frame === 'phone') + '">폰</button><button data-tray="frame" data-val="desk" aria-pressed="' + (tray.frame !== 'phone') + '">데스크톱</button></span></div>' +
        '<ol class="lb-picks">' + tray.items.map(function (x, i) {
          var miss = x.kind === 'dict';
          return '<li class="lb-pick' + (miss ? ' missing' : '') + '"><span class="nm">' + esc(x.name) + '<small>' + (miss ? '카드에 없음 · 먼저 묻기' : esc(x.id)) + '</small></span>' +
            '<span class="mv"><button data-tray="up" data-i="' + i + '" aria-label="위로"' + (i ? '' : ' disabled') + '>↑</button><button data-tray="down" data-i="' + i + '" aria-label="아래로"' + (i < n - 1 ? '' : ' disabled') + '>↓</button><button data-tray="del" data-i="' + i + '" aria-label="빼기">×</button></span></li>';
        }).join('') + '</ol>' +
        '<label class="ds-field"><span class="lbl">화면 이름 <span class="ds-muted">(선택)</span></span><input class="ds-input" id="tray-title" value="' + esc(tray.title) + '" placeholder="예: 식단표 오늘 화면"></label>' +
        '<pre class="lb-tray-ask" id="tray-ask">' + esc(trayAsk()) + '</pre>' +
        '<div class="lb-tray-act"><button class="ds-btn primary" data-copy="' + esc(trayAsk()) + '">부탁 문장 복사</button><button class="ds-btn quiet" data-tray="clear">비우기</button></div>'
        : '<p class="ds-muted" style="margin:0">블록이나 사전 항목의 [담기]로 화면에 넣을 것을 위에서 아래 순서로 담아요.</p>');
    var t = $('#tray-title');
    if (t) t.addEventListener('input', function () { tray.title = t.value; saveTray(); var a = trayAsk(); $('#tray-ask').textContent = a; box.querySelector('[data-copy]').dataset.copy = a; });
  }
  function drawTopCount() {
    var b = $('#tray-btn'); if (!b) return;
    var n = tray.items.length; b.querySelector('.badge') && b.querySelector('.badge').remove();
    if (n) b.insertAdjacentHTML('beforeend', '<span class="badge">' + n + '</span>');
    b.setAttribute('aria-label', '담은 것 ' + n);
  }

  function parse() {
    var h = location.hash.replace(/^#\/?/, ''), qs = '';
    if (h.indexOf('?') >= 0) { qs = h.slice(h.indexOf('?') + 1); h = h.slice(0, h.indexOf('?')); }
    var parts = h.split('/').filter(Boolean);
    state.sys = sysById(parts[0]) || CAT.systems[0];
    state.id = parts[1] || null;
    state.cat = new URLSearchParams(qs).get('c') || '*';
  }

  /* ── 틀: 윗줄 · 왼쪽 메뉴 · 아래 탭 ── */
  function drawTop() {
    var s = state.sys, c = state.id && s ? cardById(s, state.id) : null;
    var where = '<a href="#/' + (s ? s.id : '') + '"><b>디자인 시스템</b></a>' + (s ? '<span class="hide-phone">›</span><a class="hide-phone" href="#/' + s.id + '">' + esc(s.name) + '</a>' : '') +
      (c ? '<span>›</span><b>' + esc(c.id + ' ' + c.name) + '</b>' : '');
    $('#top').innerHTML =
      '<button class="ds-icon-btn" data-act="nav" aria-label="메뉴 접기·펴기" title="메뉴 ( [ )">' + I('menu') + '</button>' +
      '<div class="where">' + where + '</div>' +
      '<label class="find">' + I('search') + '<input id="q" placeholder="ID · 이름 · 표준 이름(바텀시트, Toast…)" aria-label="찾기" value="' + esc(state.q) + '"><kbd>/</kbd></label>' +
      '<button class="ds-icon-btn find-btn" data-act="find" aria-label="찾기">' + I('search') + '</button>' +
      '<button class="ds-icon-btn" id="add" aria-label="새 시스템" title="새 시스템">' + I('plus') + '</button>' +
      '<button class="ds-icon-btn lb-tray-btn" id="tray-btn" data-tray="toggle" title="담은 것">' + I('inbox') + '</button>' +
      '<button class="ds-icon-btn" data-act="pop" data-target="news" aria-label="새 소식" title="새 소식">' + I('bell') + '<span class="dot"></span></button>' +
      '<div class="ds-news news-pop" id="news" hidden><div class="news-h">새 소식</div><ul>' + CAT.systems.map(function (x) {
        return '<li><span class="dot"></span><div class="body"><div class="kind">' + esc(x.name) + '</div><div class="text">' + esc(x.status || '') + ' · 카드 ' + x.cards.length + ' · 예시 ' + x.cards.filter(function (c) { return c.example; }).length + '</div><div class="time">빌드 ' + esc(CAT.built) + '</div></div></li>';
      }).join('') + '</ul></div>' +
      '<button class="me" data-act="pop" data-target="me" aria-label="내 계정과 보기 설정">진</button>' +
      '<div class="ds-menu-pop" id="me" hidden><div class="who"><b>진수</b><small>personal-lab</small></div>' +
      opt('모드', 'mode', [['system', '시스템'], ['light', '밝게'], ['dark', '어둡게']]) +
      opt('보기', 'density', [['compact', '촘촘하게'], ['comfy', '편하게']]) +
      opt('폰 탭 줄', 'reduce', [['false', '유리'], ['true', '투명도 줄이기']]) + '</div>';
    var q = $('#q');
    q.addEventListener('input', function () { state.q = q.value; if (state.id) { location.hash = '#/' + state.sys.id; } else drawBlocks(); });
    q.addEventListener('keydown', function (e) {
      if (e.key === 'Enter') { var first = filtered()[0]; if (first) location.hash = '#/' + state.sys.id + '/' + first.id; }
      if (e.key === 'Escape') { q.value = ''; state.q = ''; drawBlocks(); q.blur(); }
    });
    $('#add').onclick = function () { window.MK1.toast('새 시스템: 폴더에 system.json 을 두고 python3 tools/build.py'); };
    drawTopCount();
  }
  function opt(label, key, vals) {
    var cur = String(P[key] == null ? '' : P[key]);
    return '<div class="opt"><span>' + label + '</span><span class="ds-seg">' + vals.map(function (v) {
      return '<button data-act="pref" data-key="' + key + '" data-val="' + v[0] + '" aria-pressed="' + (cur === v[0]) + '">' + v[1] + '</button>';
    }).join('') + '</span></div>';
  }
  var CAT_ICON = { P: 'compass', W: 'text', T: 'palette', C: 'box', G: 'check', L: 'layout', F: 'bolt', X: 'tool', Q: 'alert' };
  function drawNav() {
    var s = state.sys, onList = !state.id && state.cat === '*';
    function it(href, ic, full, short, n, cur) {
      return '<a class="ds-nav-it" href="' + href + '"' + (cur ? ' aria-current="page"' : '') + '>' + I(ic) + '<span class="full">' + full + '</span><span class="short">' + short + '</span>' + (n != null ? '<span class="n">' + n + '</span>' : '') + '</a>';
    }
    var html = '<div class="grp" data-grp="lb-view"><button class="grp-h">보기<span class="cv">▾</span></button><div class="items">' +
      it('#/' + s.id, 'grid', '블록 목록', '블록', null, onList) +
      (s.hasKit ? it(home(s) + '/kit.html' + pre(s, '?'), 'palette', '부품 한 장 · 조종 패널', '한 장') : '') + '</div></div>' +
      '<div class="grp" data-grp="lb-sys"><button class="grp-h">시스템<span class="cv">▾</span></button><div class="items">' +
      CAT.systems.map(function (x) { return it('#/' + x.id, 'layers', esc(x.name), esc(x.name), x.cards.length, false); }).join('') + '</div></div>';
    var cats = Object.keys(s.cats || {}).filter(function (k) { return s.cards.some(function (c) { return c.cat === k; }); });
    if (cats.length) {
      html += '<div class="grp" data-grp="lb-cats-' + s.id + '"><button class="grp-h">' + esc(s.name) + ' 묶음<span class="cv">▾</span></button><div class="items">' +
        cats.map(function (k) {
          var cur = !state.id ? state.cat === k : (cardById(s, state.id) || {}).cat === k;
          return it('#/' + s.id + '?c=' + k, CAT_ICON[k] || 'box', s.cats[k], s.cats[k], s.cards.filter(function (c) { return c.cat === k; }).length, cur);
        }).join('') + '</div></div>';
    }
    $('#nav').innerHTML = html;
    window.MK1.initFolds($('#nav'));
  }
  function drawTabs() {
    var s = state.sys;
    var tabs = [['#/' + s.id, 'grid', '블록', !state.id], ['#tray', 'inbox', '담은 것 ' + (tray.items.length || ''), false], ['#find', 'search', '찾기', false], ['#more', 'more', '더보기', false]];
    $('#tabs').innerHTML = tabs.map(function (t) {
      var a = t[0] === '#find' ? ' data-act="find-tab"' : t[0] === '#more' ? ' data-act="nav"' : t[0] === '#tray' ? ' data-tray="toggle"' : '';
      return '<a class="ds-tab" href="' + (a ? '#' : t[0]) + '"' + a + (t[3] ? ' aria-current="page"' : '') + '>' + I(t[1]) + '<span>' + t[2].trim() + '</span></a>';
    }).join('');
  }

  /* ── 대시보드 ── */
  function filtered() {
    var s = state.sys, q = state.q.trim().toLowerCase();
    return s.cards.filter(function (c) {
      if (state.cat === 'D' || (state.cat !== '*' && c.cat !== state.cat)) return false;
      if (!q) return true;
      return (s.id + '_' + c.id + ' ' + c.id + ' ' + c.name + ' ' + field(c, '하는 일') + ' ' + field(c, '다른 이름')).toLowerCase().indexOf(q) >= 0;
    });
  }
  function dictFiltered() {
    var s = state.sys, q = state.q.trim().toLowerCase();
    if (!(s.dict || []).length || (state.cat !== 'D' && !q)) return [];
    return s.dict.filter(function (d) { return !q || (d.name + ' ' + field(d, '다른 이름') + ' ' + field(d, '언제')).toLowerCase().indexOf(q) >= 0; });
  }
  function aliases(c, q) {
    var a = field(c, '다른 이름'); if (!a) return '';
    var list = a.split(/\s*,\s*/), ql = (q || '').trim().toLowerCase();
    if (ql) list.sort(function (x, y) { return (y.toLowerCase().indexOf(ql) >= 0) - (x.toLowerCase().indexOf(ql) >= 0); });
    return '<span class="lb-alias">' + list.slice(0, 3).map(function (x) {
      var h = esc(x); if (ql) { var i = x.toLowerCase().indexOf(ql); if (i >= 0) h = esc(x.slice(0, i)) + '<mark>' + esc(x.slice(i, i + ql.length)) + '</mark>' + esc(x.slice(i + ql.length)); }
      return '<span class="ds-tag">' + h + '</span>';
    }).join('') + '</span>';
  }
  function drawHome() {
    var s = state.sys;
    $('#main').innerHTML = '<h1 class="lb-h1">디자인 시스템</h1><p class="lb-sub">시스템 ' + CAT.systems.filter(function (x) { return !x.base; }).length + ' · 프리셋 ' + CAT.systems.filter(function (x) { return x.base; }).length + ' · 블록 ' + CAT.systems.reduce(function (n, x) { return n + (x.base ? 0 : x.cards.length); }, 0) + ' · 빌드 ' + esc(CAT.built) + '</p>' +
      '<div class="systems">' + CAT.systems.map(function (x) {
        var ex = x.cards.filter(function (c) { return c.example; }).length;
        return '<a class="sys-card" href="#/' + x.id + '"' + (x.id === s.id ? ' aria-current="page"' : '') + '><div class="hd"><b>' + esc(x.name) + '</b><span>' + esc(x.desc) + '</span></div>' +
          '<div class="swatches">' + (x.swatches || []).map(function (c) { return '<i style="background:' + esc(c) + '"></i>'; }).join('') + '</div>' +
          '<div class="meta">' + (x.base ? '<span>' + esc(x.base) + ' 뼈대</span>' : '') + '<span>카드 ' + x.cards.length + '</span><span>예시 ' + ex + '</span><span class="ds-st ' + (x.cards.length ? 'info' : '') + '">' + esc(x.status || '') + '</span></div></a>';
      }).join('') + '</div>' +
      '<div class="lb-bar"><h2>' + esc(s.name) + ' 블록</h2><span class="ds-seg" id="cats"></span><span class="sp"></span><span class="lb-q" id="qinfo"></span></div><div id="blocks"></div>';
    var cats = Object.keys(s.cats || {}).filter(function (k) { return s.cards.some(function (c) { return c.cat === k; }); });
    $('#cats').innerHTML = cats.length ? '<button data-c="*" aria-pressed="' + (state.cat === '*') + '">전체</button>' + cats.map(function (k) { return '<button data-c="' + k + '" aria-pressed="' + (state.cat === k) + '">' + esc(s.cats[k]) + '</button>'; }).join('') +
      ((s.dict || []).length ? '<button data-c="D" aria-pressed="' + (state.cat === 'D') + '">사전에만 있음</button>' : '') : '';
    $('#cats').onclick = function (e) { var b = e.target.closest('button'); if (b) location.hash = '#/' + s.id + (b.dataset.c === '*' ? '' : '?c=' + b.dataset.c); };
    drawBlocks();
  }
  function drawBlocks() {
    var s = state.sys, box = $('#blocks'); if (!box) return;
    if (!s.cards.length) { box.innerHTML = '<div class="lb-empty">카드가 아직 없습니다.<br><code>' + esc(s.id) + '/cards/</code> 에 카드를 더하고 <code>python3 tools/build.py</code></div>'; return; }
    var list = filtered(), dl = dictFiltered();
    $('#qinfo').textContent = state.q ? '"' + state.q + '" ' + (list.length + dl.length) + '건 · Enter 로 첫 블록 열기' : (state.cat === 'D' ? dl.length : list.length) + '건';
    box.className = 'blocks';
    box.innerHTML = list.map(function (c) {
      var id = s.id + '_' + c.id;
      var pv = c.example ? '<div class="pv"><iframe loading="lazy" tabindex="-1" aria-hidden="true" src="' + home(s) + '/examples.html?id=' + c.id + pre(s) + '&mode=' + previewMode() + '"></iframe></div>'
        : '<div class="pv none"><span class="lb-mark">' + esc(c.id) + '</span><span>' + esc((s.cats || {})[c.cat] || '') + '</span></div>';
      var on = inTray('card', c.id);
      return '<article class="blk' + (on ? ' on' : '') + '">' + pv + '<a class="body" href="#/' + s.id + '/' + c.id + '"><span class="id">' + id + '</span><span class="nm">' + esc(c.name) + '</span>' + aliases(c, state.q) + '<span class="ds">' + esc(field(c, '하는 일') || field(c, '증상') || '') + '</span></a>' +
        '<div class="ft"><span class="ds-st ' + (c.cat === 'G' ? 'info' : c.example ? 'ok' : '') + '">' + (c.cat === 'G' ? '고르는 법' : c.example ? '예시 있음' : '카드만') + '</span><span class="sp"></span>' +
        '<button class="ds-btn sm quiet" data-copy="' + esc(ask(s, c)) + '" title="이 블록만 부탁 복사">복사</button>' +
        (c.cat === 'G' ? '' : '<button class="ds-btn sm" data-pick="' + trayKey('card', c.id) + '" data-name="' + esc(c.name) + '" aria-pressed="' + on + '">' + (on ? '✓ 담음' : '담기') + '</button>') + '</div></article>';
    }).join('');
    if (dl.length) {
      box.innerHTML += '<div class="lb-dict-h"><h2>사전에만 있음</h2><span class="ds-muted">표준 부품인데 블록이 아직 없는 것. 담으면 "카드에 없음"으로 먼저 묻는다</span></div>' + dl.map(function (d) {
        var on = inTray('dict', d.name), like = field(d, '비슷한 것');
        return '<article class="blk none' + (on ? ' on' : '') + '"><div class="pv none"><span class="lb-mark">—</span><span>블록 없음</span></div><div class="body"><span class="id">사전</span><span class="nm">' + esc(d.name) + '</span>' + aliases(d, state.q) +
          '<span class="ds">' + esc(field(d, '언제')) + (like ? '<br>비슷한 것: ' + md(like, s) : '') + '</span></div>' +
          '<div class="ft"><span class="ds-st wait">카드에 없음</span><span class="sp"></span><button class="ds-btn sm" data-pick="' + trayKey('dict', d.name) + '" data-name="' + esc(d.name) + '" aria-pressed="' + on + '">' + (on ? '✓ 담음' : '담기') + '</button></div></article>';
      }).join('');
    }
    if (!box.innerHTML) box.innerHTML = '<div class="lb-empty">찾는 블록이 없습니다. 없는 부품이면 부탁할 때 "카드에 없음"으로 적어 주세요.</div>';
  }
  function previewMode() {
    if (P.mode === 'dark' || (P.mode === 'system' && window.matchMedia('(prefers-color-scheme: dark)').matches)) return 'dark';
    return 'light';
  }

  /* ── 블록 상세 ── */
  function drawDetail() {
    var s = state.sys, c = cardById(s, state.id);
    if (!c) { $('#main').innerHTML = '<div class="lb-empty">' + esc(s.id + '_' + state.id) + ' 카드가 없습니다. <a href="#/' + s.id + '">블록 목록</a></div>'; return; }
    var v = { mode: state.view.mode || previewMode(), w: state.view.w, density: state.view.density };
    var same = s.cards.filter(function (x) { return x.cat === c.cat; }), i = same.indexOf(c);
    var prev = same[i - 1], next = same[i + 1];
    var skip = ['하는 일'];
    $('#main').innerHTML =
      '<div class="ds-detail-head" style="position:static"><a class="ds-btn sm quiet" href="#/' + s.id + '?c=' + c.cat + '">' + esc(s.cats[c.cat] || c.cat) + ' 목록</a>' +
      '<span class="no">' + s.id + '_' + c.id + '</span><h1>' + esc(c.name) + '</h1><span class="ds-st ' + (c.example ? 'ok' : '') + '">' + (c.example ? '예시 있음' : '카드만') + '</span></div>' +
      '<p class="lb-sub" style="margin-top:4px">' + esc(field(c, '하는 일')) + '</p>' +
      '<div class="lb-detail"><div style="display:grid;gap:var(--sp-4);min-width:0">' +
      (c.example ? '<section class="ds-card"><div class="card-h">예시<span class="sp"></span><div class="ctrls">' +
        seg('mode', [['light', '밝게'], ['dark', '어둡게']]) + seg('w', [['desk', '데스크톱'], ['phone', '폰']]) + seg('density', [['compact', '촘촘하게'], ['comfy', '편하게']]) +
        '</div></div><div class="card-b"><div class="stage' + (v.w === 'phone' ? ' phone' : '') + '"><iframe id="exf" title="' + esc(c.name) + ' 예시" src="' + home(s) + '/examples.html?id=' + c.id + pre(s) + '&mode=' + v.mode + '&density=' + v.density + '&reduce=' + (P.reduce ? 1 : 0) + '"></iframe></div></div></section>'
        : '<section class="ds-card"><div class="card-h">예시</div><div class="card-b"><div class="lb-empty">이 블록은 카드만 있습니다. 부탁하면 예시와 함께 만듭니다.</div></div></section>') +
      '</div><section class="ds-card"><div class="card-h">카드<span class="sp"></span><a class="ds-btn sm quiet" href="' + home(s) + '/cards/' + esc(c.file) + '" target="_blank">원문</a></div><div class="card-b" style="display:grid;gap:14px">' +
      '<div class="lb-ask"><code>' + esc(ask(s, c)) + '</code><button class="ds-btn sm" data-copy="' + esc(ask(s, c)) + '">' + I('copy') + '복사</button>' +
      (c.cat === 'G' ? '' : '<button class="ds-btn sm primary" data-pick="' + trayKey('card', c.id) + '" data-name="' + esc(c.name) + '" aria-pressed="' + inTray('card', c.id) + '">' + (inTray('card', c.id) ? '✓ 담음' : '담기') + '</button>') + '</div>' +
      '<dl class="lb-spec">' + c.fields.filter(function (f) { return skip.indexOf(f[0]) < 0; }).map(function (f) { return '<div><dt>' + esc(f[0]) + '</dt><dd>' + md(f[1], s) + '</dd></div>'; }).join('') + '</dl></div></section></div>' +
      '<div class="lb-pager">' + (prev ? '<a class="ds-btn" href="#/' + s.id + '/' + prev.id + '">◀ ' + esc(prev.id + ' ' + prev.name) + '</a>' : '') + '<span class="sp"></span>' +
      (next ? '<a class="ds-btn" href="#/' + s.id + '/' + next.id + '">' + esc(next.id + ' ' + next.name) + ' ▶</a>' : '') + '</div>';
    $('#main').querySelectorAll('[data-view]').forEach(function (b) {
      b.addEventListener('click', function () { state.view[b.dataset.view] = b.dataset.val; drawDetail(); });
    });
  }
  function seg(k, vals) {
    return '<span class="ds-seg">' + vals.map(function (x) { return '<button data-view="' + k + '" data-val="' + x[0] + '" aria-pressed="' + ((k === 'mode' ? state.view.mode || previewMode() : state.view[k]) === x[0]) + '">' + x[1] + '</button>'; }).join('') + '</span>';
  }
  window.addEventListener('message', function (e) {
    var f = document.getElementById('exf');
    if (f && e.data && e.data.mk1ExHeight && e.source === f.contentWindow) f.style.height = Math.min(e.data.mk1ExHeight, 1600) + 'px';
  });

  function route() {
    parse();
    if (!state.sys) { $('#main').innerHTML = '<div class="lb-empty">시스템이 없습니다. <code>python3 tools/build.py</code></div>'; return; }
    document.title = state.id ? state.sys.id + '_' + state.id + ' · 룩북' : state.sys.name + ' · 룩북';
    document.body.classList.remove('ds-drawer-open');
    drawTop(); drawNav(); drawTabs(); drawTray();
    if (state.id) drawDetail(); else drawHome();
    window.scrollTo(0, 0);
  }
  document.addEventListener('click', function (e) {
    var ft = e.target.closest('[data-act="find-tab"]');
    if (ft) { e.preventDefault(); var bar = $('.ds-topbar'); bar.classList.add('searching'); $('#q').focus(); }
    var pref = e.target.closest('[data-act="pref"]');
    if (pref && pref.dataset.key === 'mode') { state.view.mode = null; setTimeout(route, 0); }
    if (pref && pref.dataset.key === 'density') setTimeout(route, 0);
    var pk = e.target.closest('[data-pick]');
    if (pk) { var k = pk.dataset.pick, ix = k.indexOf(':'); toggleTray(k.slice(0, ix), k.slice(ix + 1), pk.dataset.name); }
    var tb = e.target.closest('[data-tray]');
    if (tb) {
      e.preventDefault();
      var a = tb.dataset.tray, i = +tb.dataset.i, it = tray.items;
      if (a === 'toggle') document.body.classList.toggle('lb-tray-open');
      if (a === 'close') document.body.classList.remove('lb-tray-open');
      if (a === 'frame') tray.frame = tb.dataset.val;
      if (a === 'up' && i > 0) it.splice(i - 1, 0, it.splice(i, 1)[0]);
      if (a === 'down' && i < it.length - 1) it.splice(i + 1, 0, it.splice(i, 1)[0]);
      if (a === 'del') it.splice(i, 1);
      if (a === 'clear' && confirm('담은 것을 모두 비울까요?')) tray.items = [];
      if (a !== 'toggle' && a !== 'close') { saveTray(); drawTray(); drawTopCount(); drawTabs(); if (!state.id) drawBlocks(); }
    }
    if (e.target.closest('.lb-tray-bg')) document.body.classList.remove('lb-tray-open');
  });
  window.addEventListener('hashchange', route);
  route();
})();
