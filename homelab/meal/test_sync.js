// 실행: MEAL_DB=/tmp/t.db MEAL_PORT=18083 python3 server.py & 로 시험용 서버를 띄운 뒤 node test_sync.js sync.js
// sync.js를 가짜 브라우저 두 대에서 실행해 보는 시험
const vm = require("vm"), fs = require("fs"), { execFileSync } = require("child_process");
const SRC = fs.readFileSync(process.argv[2], "utf8"), ORIGIN = "http://127.0.0.1:18083";
const sleep = ms => new Promise(r => setTimeout(r, ms));
function browser(name) {
  let online = true, reloads = 0;
  class Storage {
    constructor() { this._d = {}; }
    getItem(k) { return k in this._d ? this._d[k] : null; }
    setItem(k, v) { this._d[k] = String(v); }
    removeItem(k) { delete this._d[k]; }
  }
  const ls = new Storage(), listeners = {};
  function XMLHttpRequest() {}
  XMLHttpRequest.prototype.open = function (m, u, async) { this.m = m; this.u = u; this.async = async; };
  XMLHttpRequest.prototype.send = function () {
    if (!online) return this.onerror();
    if (!this.async) {
      try { this.responseText = execFileSync("curl", ["-s", ORIGIN + this.u]).toString(); this.status = 200; this.onload(); }
      catch (e) { this.onerror(); }
    } else fetch(ORIGIN + this.u).then(r => r.text().then(t => { this.status = r.status; this.responseText = t; this.onload(); })).catch(() => this.onerror());
  };
  const ctx = {
    Storage, localStorage: ls, XMLHttpRequest, JSON, Object, Date, setInterval: () => 0,
    fetch: (u, o) => online ? fetch(ORIGIN + u, o) : Promise.reject(new Error("offline")),
    navigator: { get onLine() { return online; } },
    location: { reload() { reloads++; } },
    document: { visibilityState: "visible", addEventListener(e, f) { (listeners[e] = listeners[e] || []).push(f); }, createElement: () => ({ style: {}, setAttribute() {} }), body: { appendChild() {} } },
  };
  ctx.window = { localStorage: ls, addEventListener(e, f) { (listeners[e] = listeners[e] || []).push(f); } };
  vm.createContext(ctx);
  vm.runInContext(SRC, ctx);
  return {
    name, ls,
    // 식단표 페이지가 하는 일: 시작할 때 읽고, 바꿀 때마다 통째로 저장
    load() { return JSON.parse(vm.runInContext('localStorage.getItem("meal-2026-10-v2")', ctx) || "{}"); },
    save(state) { ctx.__s = JSON.stringify(state); vm.runInContext('localStorage.setItem("meal-2026-10-v2", __s)', ctx); },
    fire(e) { (listeners[e] || []).forEach(f => f()); },
    setOnline(v) { online = v; },
    get reloads() { return reloads; },
    pending() { return Object.keys(JSON.parse(ls.getItem("__sync:meal-2026-10-v2") || "{}").pending || {}).length; },
  };
}
function server() { return JSON.parse(execFileSync("curl", ["-s", ORIGIN + "/meal/api/doc/meal-2026-10-v2"])).state; }
let fails = 0;
function check(label, cond, extra) { console.log((cond ? "  ok    " : "  FAIL  ") + label + (cond ? "" : "  " + JSON.stringify(extra))); if (!cond) fails++; }

(async () => {
  console.log("1. 폰에 sync.js 이전 기록이 있는 상태에서 첫 연결");
  const phone = browser("phone");
  phone.ls.setItem("meal-2026-10-v2", JSON.stringify({ v: 2, slots: { "7-l": "G", "7-d": "O" }, memo: { "7": "첫날" }, shop: { s1: { ok: 1 } } }));
  let st = phone.load(); await sleep(300);
  check("기존 기록 유지", st.slots["7-l"] === "G" && st.memo["7"] === "첫날", st);
  check("서버로 올라감", server().slots && server().slots["7-d"] === "O", server());
  check("대기열 비움", phone.pending() === 0, phone.pending());

  console.log("2. PC(빈 브라우저)에서 열기");
  const pc = browser("pc");
  st = pc.load();
  check("폰 기록이 보임", st.slots && st.slots["7-l"] === "G" && st.shop.s1.ok === 1, st);

  console.log("3. PC에서 저장 → 폰이 탭으로 돌아옴");
  st.slots["8-b"] = "M"; pc.save(st); await sleep(300);
  check("서버 반영", server().slots["8-b"] === "M");
  phone.fire("visibilitychange"); await sleep(300);
  check("폰 새로 고침 1회", phone.reloads === 1, phone.reloads);
  check("폰 로컬에 반영", JSON.parse(phone.ls.getItem("meal-2026-10-v2")).slots["8-b"] === "M");

  console.log("4. 폰 오프라인에서 기록 → 다시 온라인");
  phone.setOnline(false);
  let ps = JSON.parse(phone.ls.getItem("meal-2026-10-v2")); ps.slots["8-l"] = "G"; ps.memo["8"] = "오프라인 메모"; phone.save(ps); await sleep(200);
  check("대기 2건", phone.pending() === 2, phone.pending());
  check("서버엔 아직 없음", !server().slots["8-l"]);
  phone.setOnline(true); phone.fire("online"); await sleep(400);
  check("온라인 후 서버 반영", server().slots["8-l"] === "G" && server().memo["8"] === "오프라인 메모", server());
  check("대기열 비움", phone.pending() === 0, phone.pending());

  console.log("5. 두 기기가 서로 다른 끼니를 동시에 수정");
  let a = JSON.parse(phone.ls.getItem("meal-2026-10-v2")), b = pc.load();
  b = JSON.parse(pc.ls.getItem("meal-2026-10-v2"));
  a.slots["9-l"] = "A"; b.slots["9-d"] = "B"; phone.save(a); pc.save(b); await sleep(300);
  check("둘 다 남음", server().slots["9-l"] === "A" && server().slots["9-d"] === "B", server().slots);

  console.log("6. PC 탭으로 돌아와서 기록 초기화 → 폰에도 전파");
  pc.fire("visibilitychange"); await sleep(300);
  pc.save({ v: 2, slots: {}, memo: {}, shop: {} }); await sleep(300);
  check("서버 기록 비움", Object.keys(server().slots || {}).length === 0 && !server().memo, server());
  phone.fire("visibilitychange"); await sleep(300);
  check("폰도 비워짐", Object.keys(JSON.parse(phone.ls.getItem("meal-2026-10-v2")).slots || {}).length === 0, phone.ls.getItem("meal-2026-10-v2"));

  console.log("7. 재방문(새 페이지)에서 다시 서버를 읽는가");
  const pc2 = browser("pc2"); pc2.ls._d = Object.assign({}, pc.ls._d);
  execFileSync("curl", ["-s", "-X", "POST", "-H", "Content-Type: application/json", "-d", JSON.stringify({ changes: [{ path: ["memo", "10"], value: "다른 기기", at: Date.now() }] }), ORIGIN + "/meal/api/doc/meal-2026-10-v2/changes"]);
  st = pc2.load();
  check("새 페이지가 서버 변경을 받음", st.memo && st.memo["10"] === "다른 기기", st);

  console.log(fails ? `\n실패 ${fails}건` : "\n전부 통과");
  process.exit(fails ? 1 : 0);
})();
