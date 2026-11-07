import { useEffect, useRef, useState } from "react";
import { Square, Zap } from "lucide-react";
import { Card } from "./ui";
// Requests go to the SAME ORIGIN (the Vite dev server), which proxies /api/** to the gateway on :8081
// (see vite.config.ts). Same-origin means custom headers work with no CORS/preflight problems.
export default function TrafficPanel({ running }: { running: boolean }) {
  const [path, setPath] = useState("/api/user1"); const [rps, setRps] = useState(10);
  const [hdr, setHdr] = useState("X-API-Key"); const [keyOverride, setKeyOverride] = useState("");
  const [on, setOn] = useState(false);
  const [stats, setStats] = useState({ sent: 0, ok: 0, limited: 0, other: 0, errs: 0 });
  const timer = useRef<number>(); const inflight = useRef(0);
  const autoKey = path.split("?")[0].split("/").filter(Boolean).pop() ?? ""; // /api/user2 -> user2
  const key = keyOverride || autoKey;
  const bump = (k: keyof typeof stats) => setStats((s) => ({ ...s, [k]: s[k] + 1 }));
  const stop = () => { clearInterval(timer.current); setOn(false); };
  const start = () => {
    setStats({ sent: 0, ok: 0, limited: 0, other: 0, errs: 0 }); setOn(true);
    const headers: Record<string, string> = hdr.trim() ? { [hdr.trim()]: key } : {};
    timer.current = window.setInterval(() => {
      if (inflight.current >= 50) return; // don't pile up while latency is injected
      inflight.current++; bump("sent");
      fetch(path, { headers, cache: "no-store" })
          .then((r) => bump(r.ok ? "ok" : r.status === 429 ? "limited" : "other"))
          .catch(() => bump("errs")).finally(() => { inflight.current--; });
    }, Math.max(20, 1000 / rps));
  };
  useEffect(() => { if (!running && on) stop(); }, [running, on]);
  useEffect(() => () => clearInterval(timer.current), []);
  const bad = !path.startsWith("/api/") || rps < 1 || rps > 50;
  return (
      <Card title="Send traffic through the gateway">
        <div className="grid gap-3 md:grid-cols-4">
          <label className="space-y-1 text-sm md:col-span-2"><span>Path (starts with /api/)</span><input className="inp" value={path} disabled={on} onChange={(e) => setPath(e.target.value)} /></label>
          <label className="space-y-1 text-sm"><span>Header name</span><input className="inp" value={hdr} disabled={on} onChange={(e) => setHdr(e.target.value)} /></label>
          <label className="space-y-1 text-sm"><span>Header value</span><input className="inp" value={keyOverride} placeholder={autoKey || "from path"} disabled={on} onChange={(e) => setKeyOverride(e.target.value)} /></label>
          <label className="space-y-1 text-sm"><span>Requests/sec (1–50)</span><input className="inp" type="number" min={1} max={50} value={rps} disabled={on} onChange={(e) => setRps(+e.target.value)} /></label>
          <div className="flex items-end">{on ? <button className="btn ghost" onClick={stop}><Square className="size-4" />Stop</button> : <button className="btn" disabled={bad} onClick={start}><Zap className="size-4" />Start traffic</button>}</div>
        </div>
        <p className="mt-3 text-xs text-mut">Sending <code>{hdr}: {key || "—"}</code>. Sent {stats.sent} · 2xx {stats.ok} · 429 {stats.limited} · other {stats.other} · network errors {stats.errs}. Stops automatically when the run ends. The backend's count is authoritative.</p>
      </Card>);
}
