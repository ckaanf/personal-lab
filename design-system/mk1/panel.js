/* X-01 조종 패널 — tokens.js(DS_TOKENS)로 줄을 만들고, 바꾼 값은 이 브라우저에 기억해 화면에 바로 덮는다.
   #panel 이 있는 페이지(kit.html)에서만 패널을 그리고, 다른 페이지는 기억한 값만 덮는다. 복사는 tokens.json 전체 */
(function () {
  'use strict';
  var data = window.DS_TOKENS, KEY = (data ? data.system : 'mk1') + '-token-edits', root = document.documentElement;
  if (!data) return;
  var edits = { light: {}, dark: {} };
  try { var s = JSON.parse(localStorage.getItem(KEY) || 'null'); if (s && s.light && s.dark) edits = s; } catch (e) { /* 기억 못 해도 된다 */ }
  function save() { try { localStorage.setItem(KEY, JSON.stringify(edits)); } catch (e) { /* 위와 같음 */ } }
  var all = []; data.groups.forEach(function (g) { g.tokens.forEach(function (t) { t.group = g.name; all.push(t); }); });
  function find(n) { return all.filter(function (t) { return t.name === n; })[0]; }
  function moded(t) { return 'dark' in t; }

  function apply() {
    var el = document.getElementById('mk1-edits');
    if (!el) { el = document.createElement('style'); el.id = 'mk1-edits'; document.head.appendChild(el); }
    function block(o, pick) { return Object.keys(o).filter(pick).map(function (k) { return '--' + k + ': ' + o[k] + ';'; }).join(' '); }
    function isModed(k) { var t = find(k); return t && moded(t); }
    var V = block(edits.light, function (k) { return !isModed(k); });
    var L = block(edits.light, isModed), D = block(edits.dark, function () { return true; });
    // 모드 없는 값은 늘, 밝은 값은 밝은 모드일 때만(어두운 모드 기본값을 덮지 않게)
    el.textContent = ':root { ' + V + ' }\n' +
      ':root[data-mode="light"] { ' + L + ' }\n@media (prefers-color-scheme: light) { :root:not([data-mode="dark"]) { ' + L + ' } }\n' +
      ':root[data-mode="dark"] { ' + D + ' }\n@media (prefers-color-scheme: dark) { :root:not([data-mode="light"]) { ' + D + ' } }';
  }
  apply();

  function curMode() {
    var m = root.getAttribute('data-mode');
    if (m) return m;
    return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
  }
  function bucket(t) { return moded(t) && curMode() === 'dark' ? 'dark' : 'light'; }
  function orig(t) { return moded(t) ? t[curMode() === 'dark' ? 'dark' : 'light'] : t.value; }
  function current(t) { var b = edits[bucket(t)]; return b[t.name] != null ? b[t.name] : orig(t); }

  // tokens.json 과 같은 모양으로 — 바꾼 값만 diff 에 나오게
  function serialize() {
    function line(t) {
      var o = {}; ['name', 'label', 'light', 'dark', 'value'].forEach(function (k) { if (k in t) o[k] = t[k]; });
      if ('value' in o && edits.light[t.name] != null) o.value = edits.light[t.name];
      if ('light' in o && edits.light[t.name] != null) o.light = edits.light[t.name];
      if ('dark' in o && edits.dark[t.name] != null) o.dark = edits.dark[t.name];
      return '      { ' + Object.keys(o).map(function (k) { return JSON.stringify(k) + ': ' + JSON.stringify(o[k]); }).join(', ') + ' }';
    }
    return '{\n  "system": ' + JSON.stringify(data.system) + ',\n  "note": ' + JSON.stringify(data.note) + ',\n  "groups": [\n' +
      data.groups.map(function (g) { return '    { "name": ' + JSON.stringify(g.name) + ', "tokens": [\n' + g.tokens.map(line).join(',\n') + '\n    ]}'; }).join(',\n') +
      '\n  ]\n}\n';
  }

  // 대비 — 글자처럼 읽혀야 하는 색만 잰다
  var TEXT = ['c-ink', 'c-ink-2', 'c-ink-3', 'c-primary', 'c-ok', 'c-hold', 'c-wait', 'c-danger', 'c-info', 'c-accent'];
  function rgb(v) {
    var m = /^#([0-9a-f]{3}|[0-9a-f]{6})$/i.exec((v || '').trim()); if (!m) return null;
    var h = m[1].length === 3 ? m[1].replace(/./g, '$&$&') : m[1];
    return [0, 2, 4].map(function (i) { return parseInt(h.substr(i, 2), 16) / 255; });
  }
  function lum(c) { return c.map(function (x) { return x <= 0.03928 ? x / 12.92 : Math.pow((x + 0.055) / 1.055, 2.4); }).reduce(function (s, x, i) { return s + x * [0.2126, 0.7152, 0.0722][i]; }, 0); }
  function ratio(a, b) { var x = rgb(a), y = rgb(b); if (!x || !y) return null; var l1 = lum(x), l2 = lum(y); return (Math.max(l1, l2) + 0.05) / (Math.min(l1, l2) + 0.05); }
  function contrastOf(t) {
    if (TEXT.indexOf(t.name) >= 0) return { r: ratio(current(t), current(find('c-surface'))), on: '카드 면' };
    if (t.name === 'c-on-fill') return { r: ratio(current(t), current(find('c-primary'))), on: '주 색' };
    return null;
  }

  function kind(v) {
    if (/^#[0-9a-f]{3,8}$/i.test(v)) return 'color';
    if (/^-?\d+(\.\d+)?px$/.test(v)) return 'px';
    if (/^\d+(\.\d+)?$/.test(v)) return 'num';
    return 'text';
  }
  function esc(s) { return String(s).replace(/[&<>"]/g, function (c) { return { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;' }[c]; }); }
  function ctl(t) {
    var v = current(t), k = kind(orig(t));
    if (k === 'color') return '<input type="color" data-t="' + t.name + '" value="' + esc(v.length === 4 ? v.replace(/^#(.)(.)(.)$/, '#$1$1$2$2$3$3') : v.slice(0, 7)) + '"><input type="text" data-t="' + t.name + '" value="' + esc(v) + '">';
    if (k === 'px') { var n = parseFloat(v), max = t.name === 'r-round' ? 999 : Math.max(48, Math.ceil(parseFloat(orig(t)) * 2.5)); return '<input type="range" data-t="' + t.name + '" data-u="px" min="0" max="' + max + '" step="1" value="' + n + '"><input type="number" data-t="' + t.name + '" data-u="px" value="' + n + '">'; }
    if (k === 'num') { var f = parseFloat(v), lh = t.name === 'lh'; return '<input type="range" data-t="' + t.name + '" min="' + (lh ? 1 : 100) + '" max="' + (lh ? 2.2 : 900) + '" step="' + (lh ? .05 : 100) + '" value="' + f + '"><input type="number" data-t="' + t.name + '" step="' + (lh ? .05 : 100) + '" value="' + f + '">'; }
    return '<input type="text" data-t="' + t.name + '" value="' + esc(v) + '">';
  }
  function crHtml(t) {
    var c = contrastOf(t); if (!c || c.r == null) return '';
    var bad = c.r < 4.5;
    return '<span class="cr' + (bad ? ' bad' : '') + '">' + c.on + ' 대비 ' + c.r.toFixed(1) + (bad ? ' · 4.5 미만' : '') + '</span>';
  }

  var panel = null, closed = {};
  function draw() {
    panel = document.getElementById('panel'); if (!panel) return;
    var n = Object.keys(edits.light).length + Object.keys(edits.dark).length, mode = curMode();
    panel.innerHTML = '<div class="panel-h"><div class="top"><b>값 고치기</b><button class="ds-btn sm" id="pn-reset">처음 값으로</button><button class="ds-btn sm primary" id="pn-copy">tokens.json 복사</button></div>' +
      '<div class="top"><span class="ds-muted">고치는 모드</span><span class="ds-seg"><button aria-pressed="' + (mode === 'light') + '" data-mode-set="light">밝게</button><button aria-pressed="' + (mode === 'dark') + '" data-mode-set="dark">어둡게</button></span><span class="ds-muted">바꾼 값 ' + n + '</span></div>' +
      '<div class="ds-msg" id="pn-msg"></div></div>' +
      data.groups.map(function (g) {
        return '<div class="panel-g' + (closed[g.name] ? ' closed' : '') + '" data-g="' + esc(g.name) + '"><button>' + esc(g.name) + '<span class="cv">▾</span></button><div class="panel-rows">' + g.tokens.map(function (t) {
          var wide = kind(orig(t)) === 'text', ch = edits[bucket(t)][t.name] != null;
          return '<div class="panel-r' + (wide ? ' wide' : '') + (ch ? ' changed' : '') + '" data-row="' + t.name + '"><label><span>' + esc(t.label) + (moded(t) ? '' : ' <small class="ds-muted">공통</small>') + '</span><code>--' + t.name + '</code></label><div class="ctl">' + ctl(t) + '</div>' + crHtml(t) + '</div>';
        }).join('') + '</div></div>';
      }).join('');
  }

  document.addEventListener('input', function (e) {
    var el = e.target; if (!panel || !panel.contains(el) || !el.dataset.t) return;
    var t = find(el.dataset.t), v = el.value + (el.dataset.u || ''), b = edits[bucket(t)];
    if (v === orig(t)) delete b[t.name]; else b[t.name] = v;
    panel.querySelectorAll('[data-t="' + t.name + '"]').forEach(function (o) { if (o !== el) o.value = o.dataset.u ? parseFloat(v) : (o.type === 'color' ? v.slice(0, 7) : v); });
    panel.querySelector('[data-row="' + t.name + '"]').classList.toggle('changed', b[t.name] != null);
    apply(); save();
    panel.querySelectorAll('.panel-r').forEach(function (r) { var tt = find(r.dataset.row), old = r.querySelector('.cr'), nw = crHtml(tt); if (old) old.outerHTML = nw || ''; });
  });
  document.addEventListener('click', function (e) {
    if (!panel || !panel.contains(e.target)) return;
    var g = e.target.closest('.panel-g > button'); if (g) { var pg = g.parentNode; pg.classList.toggle('closed'); closed[pg.dataset.g] = pg.classList.contains('closed'); }
    var m = e.target.closest('[data-mode-set]'); if (m) { window.MK1.setPref('mode', m.dataset.modeSet); draw(); }
    if (e.target.closest('#pn-reset')) { if (confirm('고친 값을 모두 처음 값으로 돌릴까요?')) { edits = { light: {}, dark: {} }; save(); apply(); draw(); } }
    if (e.target.closest('#pn-copy')) {
      var n = Object.keys(edits.light).length + Object.keys(edits.dark).length;
      window.MK1.copy(serialize());
      document.getElementById('pn-msg').textContent = 'tokens.json 전체를 복사했습니다. 바꾼 값 ' + n + '개. 붙여 넣고 python3 tools/build.py';
    }
  });
  document.addEventListener('DOMContentLoaded', draw);
  window.MK1Panel = { serialize: serialize, edits: function () { return edits; }, redraw: draw };
})();
