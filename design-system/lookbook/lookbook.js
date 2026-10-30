/* 룩북 — catalog.js(DS_CATALOG)를 읽어 시스템 · 블록 목록 · 블록 상세를 그린다.
   주소: #/mk1 (블록 목록) · #/mk1?c=C (묶음) · #/mk1/C-03 (상세). 부르는 이름은 늘 "시스템_카드ID" */
(function () {
  'use strict';
  var CAT = window.DS_CATALOG || { systems: [] }, I = window.MK1.icon, P = window.MK1.prefs;
  var $ = function (s) { return document.querySelector(s); };
  var state = { sys: null, cat: '*', q: '', id: null, view: { mode: null, w: 'desk', density: 'compact' } };

  function esc(s) { return String(s == null ? '' : s).replace(/[&<>"]/g, function (c) { return { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;' }[c]; }); }
  function sysById(id) { return CAT.systems.filter(function (s) { return s.id === id; })[0]; }
  function cardById(sys, id) { return sys.cards.filter(function (c) { return c.id === id; })[0]; }
  function field(c, k) { var f = c.fields.filter(function (x) { return x[0] === k; })[0]; return f ? f[1] : ''; }
  function ask(sys, c) { return sys.id + '_' + c.id + ' 구현해줘'; }
  // 카드 글 → 짧은 HTML: `코드`, **굵게**, "- " 줄은 목록, 카드 ID 는 링크
  function md(text, sys) {
    var lines = esc(text).split('\n'), out = [], list = [];
    function inline(s) {
      return s.replace(/`([^`]+)`/g, '<code>$1</code>').replace(/\*\*([^*]+)\*\*/g, '<b>$1</b>')
        .replace(/\b([PWTCLFXQ]-\d{2})\b/g, function (m) { return cardById(sys, m) ? '<a href="#/' + sys.id + '/' + m + '">' + m + '</a>' : m; });
    }
    function flush() { if (list.length) { out.push('<ul>' + list.map(function (l) { return '<li>' + inline(l) + '</li>'; }).join('') + '</ul>'); list = []; } }
    lines.forEach(function (l) { if (/^- /.test(l)) list.push(l.slice(2)); else { flush(); if (l.trim()) out.push(inline(l)); } });
    flush();
    return out.join('<br>');
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
      '<button class="icon-btn" data-act="nav" aria-label="메뉴 접기·펴기" title="메뉴 ( [ )">' + I('menu') + '</button>' +
      '<div class="where">' + where + '</div>' +
      '<label class="find">' + I('search') + '<input id="q" placeholder="카드 ID나 이름으로 찾기" aria-label="찾기" value="' + esc(state.q) + '"><kbd>/</kbd></label>' +
      '<button class="icon-btn find-btn" data-act="find" aria-label="찾기">' + I('search') + '</button>' +
      '<button class="icon-btn" id="add" aria-label="새 시스템" title="새 시스템">' + I('plus') + '</button>' +
      '<button class="icon-btn" data-act="pop" data-target="news" aria-label="새 소식" title="새 소식">' + I('bell') + '<span class="dot"></span></button>' +
      '<div class="news news-pop" id="news" hidden><div class="news-h">새 소식</div><ul>' + CAT.systems.map(function (x) {
        return '<li><span class="dot"></span><div class="body"><div class="kind">' + esc(x.name) + '</div><div class="text">' + esc(x.status || '') + ' · 카드 ' + x.cards.length + ' · 예시 ' + x.cards.filter(function (c) { return c.example; }).length + '</div><div class="time">빌드 ' + esc(CAT.built) + '</div></div></li>';
      }).join('') + '</ul></div>' +
      '<button class="me" data-act="pop" data-target="me" aria-label="내 계정과 보기 설정">진</button>' +
      '<div class="menu-pop" id="me" hidden><div class="who"><b>진수</b><small>personal-lab</small></div>' +
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
  }
  function opt(label, key, vals) {
    var cur = String(P[key] == null ? '' : P[key]);
    return '<div class="opt"><span>' + label + '</span><span class="seg">' + vals.map(function (v) {
      return '<button data-act="pref" data-key="' + key + '" data-val="' + v[0] + '" aria-pressed="' + (cur === v[0]) + '">' + v[1] + '</button>';
    }).join('') + '</span></div>';
  }
  var CAT_ICON = { P: 'compass', W: 'text', T: 'palette', C: 'box', L: 'layout', F: 'bolt', X: 'tool', Q: 'alert' };
  function drawNav() {
    var s = state.sys, onList = !state.id && state.cat === '*';
    function it(href, ic, full, short, n, cur) {
      return '<a class="nav-it" href="' + href + '"' + (cur ? ' aria-current="page"' : '') + '>' + I(ic) + '<span class="full">' + full + '</span><span class="short">' + short + '</span>' + (n != null ? '<span class="n">' + n + '</span>' : '') + '</a>';
    }
    var html = '<div class="grp" data-grp="lb-view"><button class="grp-h">보기<span class="cv">▾</span></button><div class="items">' +
      it('#/' + s.id, 'grid', '블록 목록', '블록', null, onList) +
      (s.hasKit ? it(s.id + '/kit.html', 'palette', '부품 한 장 · 조종 패널', '한 장') : '') + '</div></div>' +
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
    var tabs = [['#/' + s.id, 'grid', '블록', !state.id], [s.hasKit ? s.id + '/kit.html' : '#/' + s.id, 'palette', '한 장', false], ['#find', 'search', '찾기', false], ['#more', 'more', '더보기', false]];
    $('#tabs').innerHTML = tabs.map(function (t) {
      var a = t[0] === '#find' ? ' data-act="find-tab"' : t[0] === '#more' ? ' data-act="nav"' : '';
      return '<a class="tab" href="' + (a ? '#' : t[0]) + '"' + a + (t[3] ? ' aria-current="page"' : '') + '>' + I(t[1]) + '<span>' + t[2] + '</span></a>';
    }).join('');
  }

  /* ── 대시보드 ── */
  function filtered() {
    var s = state.sys, q = state.q.trim().toLowerCase();
    return s.cards.filter(function (c) {
      if (state.cat !== '*' && c.cat !== state.cat) return false;
      if (!q) return true;
      return (s.id + '_' + c.id + ' ' + c.id + ' ' + c.name + ' ' + field(c, '하는 일')).toLowerCase().indexOf(q) >= 0;
    });
  }
  function drawHome() {
    var s = state.sys;
    $('#main').innerHTML = '<h1 class="lb-h1">디자인 시스템</h1><p class="lb-sub">시스템 ' + CAT.systems.length + ' · 블록 ' + CAT.systems.reduce(function (n, x) { return n + x.cards.length; }, 0) + ' · 빌드 ' + esc(CAT.built) + '</p>' +
      '<div class="systems">' + CAT.systems.map(function (x) {
        var ex = x.cards.filter(function (c) { return c.example; }).length;
        return '<a class="sys-card" href="#/' + x.id + '"' + (x.id === s.id ? ' aria-current="page"' : '') + '><div class="hd"><b>' + esc(x.name) + '</b><span>' + esc(x.desc) + '</span></div>' +
          '<div class="swatches">' + (x.swatches || []).map(function (c) { return '<i style="background:' + esc(c) + '"></i>'; }).join('') + '</div>' +
          '<div class="meta"><span>카드 ' + x.cards.length + '</span><span>예시 ' + ex + '</span><span class="st ' + (x.cards.length ? 'info' : '') + '">' + esc(x.status || '') + '</span></div></a>';
      }).join('') + '</div>' +
      '<div class="lb-bar"><h2>' + esc(s.name) + ' 블록</h2><span class="seg" id="cats"></span><span class="sp"></span><span class="lb-q" id="qinfo"></span></div><div id="blocks"></div>';
    var cats = Object.keys(s.cats || {}).filter(function (k) { return s.cards.some(function (c) { return c.cat === k; }); });
    $('#cats').innerHTML = cats.length ? '<button data-c="*" aria-pressed="' + (state.cat === '*') + '">전체</button>' + cats.map(function (k) { return '<button data-c="' + k + '" aria-pressed="' + (state.cat === k) + '">' + esc(s.cats[k]) + '</button>'; }).join('') : '';
    $('#cats').onclick = function (e) { var b = e.target.closest('button'); if (b) location.hash = '#/' + s.id + (b.dataset.c === '*' ? '' : '?c=' + b.dataset.c); };
    drawBlocks();
  }
  function drawBlocks() {
    var s = state.sys, box = $('#blocks'); if (!box) return;
    if (!s.cards.length) { box.innerHTML = '<div class="lb-empty">카드가 아직 없습니다.<br><code>' + esc(s.id) + '/cards/</code> 에 카드를 더하고 <code>python3 tools/build.py</code></div>'; return; }
    var list = filtered();
    $('#qinfo').textContent = state.q ? '"' + state.q + '" ' + list.length + '건 · Enter 로 첫 블록 열기' : list.length + '건';
    box.className = 'blocks';
    box.innerHTML = list.map(function (c) {
      var id = s.id + '_' + c.id;
      var pv = c.example ? '<div class="pv"><iframe loading="lazy" tabindex="-1" aria-hidden="true" src="' + s.id + '/examples.html?id=' + c.id + '&mode=' + previewMode() + '"></iframe></div>'
        : '<div class="pv none"><span class="lb-mark">' + esc(c.id) + '</span><span>' + esc((s.cats || {})[c.cat] || '') + '</span></div>';
      return '<article class="blk">' + pv + '<a class="body" href="#/' + s.id + '/' + c.id + '"><span class="id">' + id + '</span><span class="nm">' + esc(c.name) + '</span><span class="ds">' + esc(field(c, '하는 일') || field(c, '증상') || '') + '</span></a>' +
        '<div class="ft"><span class="st ' + (c.example ? 'ok' : '') + '">' + (c.example ? '예시 있음' : '카드만') + '</span><span class="sp"></span><button class="btn sm" data-copy="' + esc(ask(s, c)) + '">부탁 복사</button></div></article>';
    }).join('') || '<div class="lb-empty">찾는 블록이 없습니다.</div>';
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
      '<div class="detail-head" style="position:static"><a class="btn sm quiet" href="#/' + s.id + '?c=' + c.cat + '">' + esc(s.cats[c.cat] || c.cat) + ' 목록</a>' +
      '<span class="no">' + s.id + '_' + c.id + '</span><h1>' + esc(c.name) + '</h1><span class="st ' + (c.example ? 'ok' : '') + '">' + (c.example ? '예시 있음' : '카드만') + '</span></div>' +
      '<p class="lb-sub" style="margin-top:4px">' + esc(field(c, '하는 일')) + '</p>' +
      '<div class="lb-detail"><div style="display:grid;gap:var(--sp-4);min-width:0">' +
      (c.example ? '<section class="card"><div class="card-h">예시<span class="sp"></span><div class="ctrls">' +
        seg('mode', [['light', '밝게'], ['dark', '어둡게']]) + seg('w', [['desk', '데스크톱'], ['phone', '폰']]) + seg('density', [['compact', '촘촘하게'], ['comfy', '편하게']]) +
        '</div></div><div class="card-b"><div class="stage' + (v.w === 'phone' ? ' phone' : '') + '"><iframe id="exf" title="' + esc(c.name) + ' 예시" src="' + s.id + '/examples.html?id=' + c.id + '&mode=' + v.mode + '&density=' + v.density + '&reduce=' + (P.reduce ? 1 : 0) + '"></iframe></div></div></section>'
        : '<section class="card"><div class="card-h">예시</div><div class="card-b"><div class="lb-empty">이 블록은 카드만 있습니다. 부탁하면 예시와 함께 만듭니다.</div></div></section>') +
      '</div><section class="card"><div class="card-h">카드<span class="sp"></span><a class="btn sm quiet" href="' + s.id + '/cards/' + esc(c.file) + '" target="_blank">원문</a></div><div class="card-b" style="display:grid;gap:14px">' +
      '<div class="lb-ask"><code>' + esc(ask(s, c)) + '</code><button class="btn sm primary" data-copy="' + esc(ask(s, c)) + '">' + I('copy') + '복사</button></div>' +
      '<dl class="lb-spec">' + c.fields.filter(function (f) { return skip.indexOf(f[0]) < 0; }).map(function (f) { return '<div><dt>' + esc(f[0]) + '</dt><dd>' + md(f[1], s) + '</dd></div>'; }).join('') + '</dl></div></section></div>' +
      '<div class="lb-pager">' + (prev ? '<a class="btn" href="#/' + s.id + '/' + prev.id + '">◀ ' + esc(prev.id + ' ' + prev.name) + '</a>' : '') + '<span class="sp"></span>' +
      (next ? '<a class="btn" href="#/' + s.id + '/' + next.id + '">' + esc(next.id + ' ' + next.name) + ' ▶</a>' : '') + '</div>';
    $('#main').querySelectorAll('[data-view]').forEach(function (b) {
      b.addEventListener('click', function () { state.view[b.dataset.view] = b.dataset.val; drawDetail(); });
    });
  }
  function seg(k, vals) {
    return '<span class="seg">' + vals.map(function (x) { return '<button data-view="' + k + '" data-val="' + x[0] + '" aria-pressed="' + ((k === 'mode' ? state.view.mode || previewMode() : state.view[k]) === x[0]) + '">' + x[1] + '</button>'; }).join('') + '</span>';
  }
  window.addEventListener('message', function (e) {
    var f = document.getElementById('exf');
    if (f && e.data && e.data.mk1ExHeight && e.source === f.contentWindow) f.style.height = Math.min(e.data.mk1ExHeight, 1600) + 'px';
  });

  function route() {
    parse();
    if (!state.sys) { $('#main').innerHTML = '<div class="lb-empty">시스템이 없습니다. <code>python3 tools/build.py</code></div>'; return; }
    document.title = state.id ? state.sys.id + '_' + state.id + ' · 룩북' : state.sys.name + ' · 룩북';
    document.body.classList.remove('drawer-open');
    drawTop(); drawNav(); drawTabs();
    if (state.id) drawDetail(); else drawHome();
    window.scrollTo(0, 0);
  }
  document.addEventListener('click', function (e) {
    var ft = e.target.closest('[data-act="find-tab"]');
    if (ft) { e.preventDefault(); var bar = $('.topbar'); bar.classList.add('searching'); $('#q').focus(); }
    var pref = e.target.closest('[data-act="pref"]');
    if (pref && pref.dataset.key === 'mode') { state.view.mode = null; setTimeout(route, 0); }
    if (pref && pref.dataset.key === 'density') setTimeout(route, 0);
  });
  window.addEventListener('hashchange', route);
  route();
})();
