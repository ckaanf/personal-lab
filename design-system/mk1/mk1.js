/* mk1 부품 동작 — 아이콘, 보기 설정 기억, 고르기·접기·메뉴·서랍·찾기·복사. 의존성 없음 */
(function () {
  'use strict';
  var KEY = 'mk1-prefs';
  var root = document.documentElement;

  var PATHS = {
    menu: '<path d="M4 6h16M4 12h16M4 18h16"/>',
    search: '<circle cx="11" cy="11" r="7"/><path d="m20 20-3.5-3.5"/>',
    plus: '<path d="M12 5v14M5 12h14"/>',
    bell: '<path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9"/><path d="M10.3 21a1.94 1.94 0 0 0 3.4 0"/>',
    home: '<path d="M3 11l9-7 9 7v9a1 1 0 0 1-1 1h-5v-6H9v6H4a1 1 0 0 1-1-1z"/>',
    layers: '<path d="m12 3 9 5-9 5-9-5z"/><path d="m3 13 9 5 9-5"/>',
    box: '<rect x="4" y="4" width="16" height="16" rx="3"/><path d="M4 10h16"/>',
    layout: '<rect x="3" y="4" width="18" height="16" rx="2"/><path d="M9 4v16"/>',
    bolt: '<path d="M13 2 4 14h7l-1 8 9-12h-7z"/>',
    tool: '<path d="M14.7 6.3a4 4 0 0 0-5.4 5.4L3 18l3 3 6.3-6.3a4 4 0 0 0 5.4-5.4l-2.5 2.5-2.4-.6-.6-2.4z"/>',
    palette: '<circle cx="12" cy="12" r="9"/><circle cx="8" cy="10" r="1.3"/><circle cx="12" cy="7.5" r="1.3"/><circle cx="16" cy="10" r="1.3"/>',
    alert: '<path d="M12 3 2 20h20z"/><path d="M12 10v4M12 17h.01"/>',
    text: '<path d="M4 7V5h16v2M12 5v14M9 19h6"/>',
    compass: '<circle cx="12" cy="12" r="9"/><path d="m15 9-2 5-4 1 2-5z"/>',
    grid: '<rect x="4" y="4" width="7" height="7" rx="1.5"/><rect x="13" y="4" width="7" height="7" rx="1.5"/><rect x="4" y="13" width="7" height="7" rx="1.5"/><rect x="13" y="13" width="7" height="7" rx="1.5"/>',
    more: '<circle cx="5" cy="12" r="1.5"/><circle cx="12" cy="12" r="1.5"/><circle cx="19" cy="12" r="1.5"/>',
    inbox: '<path d="M3 13h5l2 3h4l2-3h5"/><path d="M5 5h14l2 8v6H3v-6z"/>',
    chat: '<path d="M4 5h16v11H9l-5 4z"/>',
    check: '<path d="m5 12 5 5 9-10"/>',
    copy: '<rect x="8" y="8" width="12" height="12" rx="2"/><path d="M16 8V5a1 1 0 0 0-1-1H5a1 1 0 0 0-1 1v10a1 1 0 0 0 1 1h3"/>',
    chevron: '<path d="m6 9 6 6 6-6"/>',
    info: '<circle cx="12" cy="12" r="9"/><path d="M12 11v5M12 8h.01"/>',
    user: '<circle cx="12" cy="8" r="4"/><path d="M4 21c1.5-4 4.5-6 8-6s6.5 2 8 6"/>'
  };
  function icon(name, cls) {
    return '<svg class="icon' + (cls ? ' ' + cls : '') + '" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">' + (PATHS[name] || '') + '</svg>';
  }

  // 보기 설정 — 모드(system|light|dark) · 보기(compact|comfy) · 투명도 줄이기 · 왼쪽 메뉴 접힘
  var prefs = { mode: 'system', density: 'compact', reduce: false, rail: false, fold: {} };
  try { Object.assign(prefs, JSON.parse(localStorage.getItem(KEY) || '{}')); } catch (e) { /* 기억 못 해도 화면은 된다 */ }
  function save() { try { localStorage.setItem(KEY, JSON.stringify(prefs)); } catch (e) { /* 위와 같음 */ } }
  function applyPrefs() {
    var q = new URLSearchParams(location.search);
    var mode = q.get('mode') || prefs.mode, density = q.get('density') || prefs.density;
    if (mode === 'system') root.removeAttribute('data-mode'); else root.setAttribute('data-mode', mode);
    root.setAttribute('data-density', density);
    root.classList.toggle('reduce-transparency', q.get('reduce') === '1' || (!q.has('reduce') && !!prefs.reduce));
    if (document.body) document.body.classList.toggle('rail', !!prefs.rail);
  }
  function setPref(k, v) { prefs[k] = v; save(); applyPrefs(); }
  applyPrefs();

  function toast(msg) {
    var t = document.querySelector('.toast');
    if (!t) { t = document.createElement('div'); t.className = 'toast'; t.setAttribute('role', 'status'); document.body.appendChild(t); }
    t.textContent = msg; t.classList.add('show');
    clearTimeout(toast.timer); toast.timer = setTimeout(function () { t.classList.remove('show'); }, 1800);
  }
  function copy(text) {
    function done() { toast('복사했습니다: ' + (text.length > 40 ? text.slice(0, 40) + '…' : text)); }
    function fallback() {
      var ta = document.createElement('textarea'); ta.value = text; ta.style.position = 'fixed'; ta.style.opacity = '0'; document.body.appendChild(ta); ta.select();
      try { document.execCommand('copy'); } catch (e) { /* 복사 못 하면 그대로 둔다 */ } ta.remove(); done();
    }
    if (navigator.clipboard && navigator.clipboard.writeText) navigator.clipboard.writeText(text).then(done, fallback); else fallback();
  }

  function isPhone(el) { return (el && el.closest('.phone-frame')) || window.matchMedia('(max-width: 899px)').matches; }
  function toggleNav(el) {
    var frame = el.closest('.phone-frame');
    if (frame) { frame.classList.toggle('drawer-open'); return; }
    if (isPhone(el)) { document.body.classList.toggle('drawer-open'); return; }
    setPref('rail', !prefs.rail);
  }

  function initFolds(scope) {
    (scope || document).querySelectorAll('.grp[data-grp]').forEach(function (g) {
      var k = 'grp:' + g.dataset.grp;
      if (g.querySelector('[aria-current="page"]')) g.classList.remove('closed');
      else if (prefs.fold[k] != null) g.classList.toggle('closed', prefs.fold[k]);
    });
    (scope || document).querySelectorAll('.card[data-fold]').forEach(function (c) {
      var k = 'card:' + c.dataset.fold;
      if (prefs.fold[k] != null) c.classList.toggle('folded', prefs.fold[k]);
    });
  }

  document.addEventListener('click', function (e) {
    var t = e.target;
    var seg = t.closest('.seg button');
    if (seg) {
      seg.parentNode.querySelectorAll('button').forEach(function (b) { b.setAttribute('aria-pressed', String(b === seg)); });
      var cmp = seg.closest('.compose[data-audience]');
      if (cmp && seg.dataset.aud) setAudience(cmp, seg.dataset.aud);
    }
    var gh = t.closest('.grp-h');
    if (gh) { var g = gh.closest('.grp'); g.classList.toggle('closed'); if (g.dataset.grp) { prefs.fold['grp:' + g.dataset.grp] = g.classList.contains('closed'); save(); } }
    var ch = t.closest('.card[data-fold] > .card-h');
    if (ch && !t.closest('button:not(.fold-btn), a, input, .seg')) { var c = ch.parentNode; c.classList.toggle('folded'); prefs.fold['card:' + c.dataset.fold] = c.classList.contains('folded'); save(); }
    var act = t.closest('[data-act]');
    if (act) {
      var a = act.dataset.act;
      if (a === 'nav') toggleNav(act);
      if (a === 'find') { var bar = act.closest('.topbar'); bar.classList.add('searching'); var inp = bar.querySelector('.find input'); if (inp) inp.focus(); }
      if (a === 'pop') { var pop = document.getElementById(act.dataset.target); if (pop) { pop.hidden = !pop.hidden; e.stopPropagation(); } }
      if (a === 'info') document.body.classList.toggle('info-open');
      if (a === 'pref') setPref(act.dataset.key, act.dataset.val === 'true' ? true : act.dataset.val === 'false' ? false : act.dataset.val);
    }
    if (t.closest('.drawer-bg')) { var fr = t.closest('.phone-frame'); (fr || document.body).classList.remove('drawer-open'); }
    var tab = t.closest('.tab');
    if (tab && tab.getAttribute('href') === '#') { e.preventDefault(); tab.parentNode.querySelectorAll('.tab').forEach(function (x) { x.removeAttribute('aria-current'); }); tab.setAttribute('aria-current', 'page'); }
    var cp = t.closest('[data-copy]');
    if (cp) copy(cp.dataset.copy);
    var once = t.closest('[data-once]');
    if (once) { if (once.getAttribute('aria-busy') === 'true') { e.preventDefault(); return; } once.setAttribute('aria-busy', 'true'); var label = once.textContent; once.textContent = '보내는 중'; setTimeout(function () { once.removeAttribute('aria-busy'); once.textContent = label; toast('한 번만 보냈습니다'); }, 1200); }
    document.querySelectorAll('.menu-pop:not([hidden]), .news-pop:not([hidden])').forEach(function (p) { if (!p.contains(t) && !t.closest('[data-act="pop"]')) p.hidden = true; });
  });

  function setAudience(cmp, aud) {
    var pub = aud === 'public', who = cmp.dataset.audience || '상대';
    cmp.classList.toggle('internal', !pub);
    var warn = cmp.querySelector('.warn'); if (warn) warn.hidden = !pub;
    if (warn) warn.textContent = who + '에게 보입니다';
    var send = cmp.querySelector('[data-send]'); if (send) send.textContent = pub ? who + '에게 보내기' : '메모 남기기';
  }

  document.addEventListener('keydown', function (e) {
    var typing = /INPUT|TEXTAREA|SELECT/.test(document.activeElement.tagName) || document.activeElement.isContentEditable;
    if (e.key === '/' && !typing) { var f = document.querySelector('.topbar .find input'); if (f) { e.preventDefault(); var bar = f.closest('.topbar'); bar.classList.add('searching'); f.focus(); } }
    if (e.key === '[' && !typing) { var b = document.querySelector('[data-act="nav"]'); if (b) toggleNav(b); }
    if (e.key === ']' && !typing && document.querySelector('.info-panel')) document.body.classList.toggle('info-open');
    if (e.key === 'Escape') {
      document.querySelectorAll('.menu-pop, .news-pop').forEach(function (p) { p.hidden = true; });
      document.querySelectorAll('.topbar.searching').forEach(function (b) { b.classList.remove('searching'); });
      document.body.classList.remove('drawer-open', 'info-open');
    }
    if ((e.metaKey || e.ctrlKey) && e.key === 'Enter') { var cmp = document.activeElement.closest && document.activeElement.closest('.compose'); if (cmp) { var s = cmp.querySelector('[data-send]'); if (s) s.click(); } }
  });
  document.addEventListener('focusout', function (e) { var bar = e.target.closest && e.target.closest('.topbar.searching'); if (bar && !bar.contains(e.relatedTarget)) bar.classList.remove('searching'); });
  document.addEventListener('DOMContentLoaded', function () {
    applyPrefs(); initFolds();
    document.querySelectorAll('.compose[data-audience]').forEach(function (c) { setAudience(c, 'internal'); });
  });

  window.MK1 = { icon: icon, prefs: prefs, setPref: setPref, toast: toast, copy: copy, initFolds: initFolds, setAudience: setAudience };
})();
