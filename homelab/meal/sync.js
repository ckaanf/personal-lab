// 식단표 기록을 서버(meal-api)와 동기화한다. 식단표 HTML은 고치지 않는다.
//
// 식단표는 기록을 localStorage의 "meal-YYYY-MM-..." 키에 JSON 하나로 저장한다.
// 이 스크립트는 페이지 스크립트보다 먼저 실행되어 localStorage를 감싼다.
//   - 처음 읽을 때: 서버 기록을 받아 로컬과 합친 뒤 돌려준다.
//   - 저장할 때: 바뀐 "섹션/키"만 골라 서버로 보낸다. 실패하면 대기열에 두고 다시 보낸다.
//   - 탭으로 돌아올 때: 다른 기기에서 바뀐 게 있으면 새로 고친다.
(function () {
  "use strict";
  var API = "/meal/api/doc/";
  var PREFIX = "meal-";
  var ls = window.localStorage;
  var P = Storage.prototype;
  var rawGet = P.getItem, rawSet = P.setItem, rawRemove = P.removeItem;
  var metas = {};
  var loaded = {};  // 이번 페이지에서 서버와 맞춘 문서 (저장하지 않음)
  var badge = null;

  function isDoc(store, key) { return store === ls && typeof key === "string" && key.indexOf(PREFIX) === 0; }
  function parse(s) { try { return s ? JSON.parse(s) : {}; } catch (e) { return {}; } }
  function isObj(v) { return v !== null && typeof v === "object" && !Array.isArray(v); }

  // {slots: {"7-l": x}, v: 2} -> {"slots\u0000" + "7-l": "x", "v": "2"} (값은 JSON 문자열)
  var SEP = "\u0000";
  function flatten(state) {
    var out = {};
    Object.keys(state).forEach(function (k) {
      if (isObj(state[k])) Object.keys(state[k]).forEach(function (ik) { out[k + SEP + ik] = JSON.stringify(state[k][ik]); });
      else out[k] = JSON.stringify(state[k]);
    });
    return out;
  }
  function unflatten(flat) {
    var out = {};
    Object.keys(flat).forEach(function (p) {
      var i = p.indexOf(SEP), v = JSON.parse(flat[p]);
      if (i < 0) out[p] = v;
      else (out[p.slice(0, i)] = out[p.slice(0, i)] || {})[p.slice(i + 1)] = v;
    });
    return out;
  }
  function toPath(p) { var i = p.indexOf(SEP); return i < 0 ? [p] : [p.slice(0, i), p.slice(i + 1)]; }
  function same(a, b) {
    var ka = Object.keys(a), kb = Object.keys(b);
    return ka.length === kb.length && ka.every(function (k) { return a[k] === b[k]; });
  }

  // 문서별 상태: snap = 마지막으로 본 서버 기록, pending = 아직 서버에 못 보낸 변경
  function meta(doc) {
    if (!metas[doc]) metas[doc] = parse(rawGet.call(ls, "__sync:" + doc));
    if (!metas[doc].pending) metas[doc].pending = {};
    return metas[doc];
  }
  function saveMeta(doc) { try { rawSet.call(ls, "__sync:" + doc, JSON.stringify(metas[doc])); } catch (e) {} }

  // 서버 기록 위에 아직 못 보낸 내 변경을 얹는다. 처음 연결이면 로컬에만 있던 기록도 올린다.
  function merge(doc, server) {
    var m = meta(doc), S = flatten(server), L = flatten(parse(rawGet.call(ls, doc)));
    var out = Object.assign({}, S);
    if (!m.snap) {
      Object.keys(L).forEach(function (p) {
        // 아주 오래된 시각(1)으로 올려서, 서버에 같은 키가 이미 있으면 서버 값이 이긴다
        if (!(p in S) && !(p in m.pending)) { out[p] = L[p]; m.pending[p] = { value: JSON.parse(L[p]), at: 1 }; }
      });
    }
    Object.keys(m.pending).forEach(function (p) {
      if (m.pending[p].value === null) delete out[p]; else out[p] = JSON.stringify(m.pending[p].value);
    });
    m.snap = S;
    saveMeta(doc);
    return out;
  }

  function fetchDoc(doc, sync, done) {
    var x = new XMLHttpRequest();
    x.open("GET", API + doc, !sync);
    x.onload = function () { done(x.status === 200 ? JSON.parse(x.responseText).state : null); };
    x.onerror = function () { done(null); };
    try { x.send(); } catch (e) { done(null); }
  }

  var flushing = {};
  function flush(doc) {
    var m = meta(doc), paths = Object.keys(m.pending);
    if (!paths.length || flushing[doc]) return status();
    flushing[doc] = true;
    var sent = {};
    var changes = paths.map(function (p) {
      sent[p] = m.pending[p].at;
      return { path: toPath(p), value: m.pending[p].value, at: m.pending[p].at };
    });
    status("저장 중…");
    fetch(API + doc + "/changes", {
      method: "POST", headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ changes: changes })
    }).then(function (r) {
      if (!r.ok) throw new Error(r.status);
      Object.keys(sent).forEach(function (p) {
        if (!m.pending[p] || m.pending[p].at !== sent[p]) return;  // 보내는 사이 또 바뀜
        if (m.pending[p].value === null) delete m.snap[p]; else m.snap[p] = JSON.stringify(m.pending[p].value);
        delete m.pending[p];
      });
      saveMeta(doc);
    }).catch(function () {}).then(function () { flushing[doc] = false; status(); });
  }

  P.getItem = function (key) {
    if (!isDoc(this, key) || loaded[key]) return rawGet.apply(this, arguments);
    loaded[key] = true;
    fetchDoc(key, true, function (server) {
      if (server) { rawSet.call(ls, key, JSON.stringify(unflatten(merge(key, server)))); flush(key); }
      else status();
    });
    return rawGet.call(ls, key);
  };

  P.setItem = function (key, value) {
    if (!isDoc(this, key)) return rawSet.apply(this, arguments);
    var before = flatten(parse(rawGet.call(ls, key))), after = flatten(parse(value)), m = meta(key), now = Date.now();
    rawSet.call(ls, key, value);
    Object.keys(after).forEach(function (p) { if (before[p] !== after[p]) m.pending[p] = { value: JSON.parse(after[p]), at: now }; });
    Object.keys(before).forEach(function (p) { if (!(p in after)) m.pending[p] = { value: null, at: now }; });
    saveMeta(key);
    flush(key);
  };

  P.removeItem = function (key) {
    if (!isDoc(this, key)) return rawRemove.apply(this, arguments);
    P.setItem.call(this, key, "{}");
  };

  // 탭으로 돌아오거나 다시 온라인이 되면: 못 보낸 것 보내고, 다른 기기 변경을 받아온다
  function refresh() {
    Object.keys(loaded).forEach(function (doc) {
      flush(doc);
      fetchDoc(doc, false, function (server) {
        if (!server) return status();
        var current = flatten(parse(rawGet.call(ls, doc))), merged = merge(doc, server);
        if (!same(current, merged)) { rawSet.call(ls, doc, JSON.stringify(unflatten(merged))); location.reload(); }
        else status();
      });
    });
  }
  document.addEventListener("visibilitychange", function () { if (document.visibilityState === "visible") refresh(); });
  window.addEventListener("online", refresh);
  setInterval(function () { Object.keys(metas).forEach(flush); }, 30000);

  // 화면 오른쪽 아래의 작은 동기화 표시
  function status(text) {
    if (!badge) return;
    if (!text) {
      var waiting = Object.keys(metas).reduce(function (n, d) { return n + Object.keys(metas[d].pending).length; }, 0);
      text = !navigator.onLine ? "오프라인 · 대기 " + waiting
        : waiting ? "동기화 대기 " + waiting
        : "동기화됨 " + new Date().toTimeString().slice(0, 5);
    }
    badge.textContent = text;
  }
  document.addEventListener("DOMContentLoaded", function () {
    badge = document.createElement("div");
    badge.setAttribute("aria-live", "polite");
    badge.style.cssText = "position:fixed;right:8px;bottom:8px;z-index:9999;font:12px/1.4 system-ui,sans-serif;" +
      "padding:3px 8px;border-radius:999px;background:rgba(127,127,127,.18);color:inherit;pointer-events:none";
    document.body.appendChild(badge);
    status();
  });
})();
