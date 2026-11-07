import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Play } from "lucide-react";
import { api, errMsg } from "../api";
import { useExperiments } from "../hooks";
import { Card, ErrorBox } from "../components/ui";
import type { FaultType } from "../types";
const L = ({ t, hint, children }: { t: string; hint?: string; children: React.ReactNode }) => <label className="block space-y-1"><span className="text-sm">{t} <span className="text-bad">*</span></span>{children}{hint && <span className="block text-xs text-mut">{hint}</span>}</label>;
export default function Create() {
  const nav = useNavigate(); const { active } = useExperiments(3000);
  const [name, setName] = useState(""); const [fault, setFault] = useState<FaultType>("LATENCY"); const [pctV, setPct] = useState(50); const [dur, setDur] = useState(30);
  const [lat, setLat] = useState(500); const [code, setCode] = useState(503); const [baseline, setBaseline] = useState(false);
  const [busy, setBusy] = useState(false); const [err, setErr] = useState<string | null>(null);
  const bad = !name.trim() || dur < 1 || pctV < 0 || pctV > 100 || (fault === "LATENCY" && lat < 0) || (fault === "ERROR" && (code < 100 || code > 599));
  async function submit(e: React.FormEvent) {
    e.preventDefault(); if (busy || bad || active) return; setBusy(true); setErr(null);
    try { const r = await api.create({ name: name.trim(), faultType: fault, percentage: baseline ? 0 : pctV, durationSeconds: dur, latencyMs: fault === "LATENCY" ? lat : 0, errorCode: fault === "ERROR" ? code : 0, active: true }); nav(`/experiments/${r.id}`); }
    catch (x) { setErr(errMsg(x)); setBusy(false); }
  }
  return (
    <form onSubmit={submit} className="mx-auto max-w-xl space-y-4">
      {active && <ErrorBox msg={`"${active.name}" is still running. Starting another would orphan it, so wait until it finishes.`} />}
      {err && <ErrorBox msg={err} />}
      <Card title="Experiment configuration"><div className="space-y-4">
        <L t="Name"><input className="inp" value={name} onChange={(e) => setName(e.target.value)} placeholder="e.g. Redis latency 500ms" /></L>
        <label className="flex items-center gap-2 text-sm"><input type="checkbox" checked={baseline} onChange={(e) => setBaseline(e.target.checked)} />Baseline run (0% faults — compare other runs against this)</label>
        <L t="Fault type"><select className="inp" value={fault} onChange={(e) => setFault(e.target.value as FaultType)}><option value="LATENCY">LATENCY — delay requests</option><option value="ERROR">ERROR — return an error status</option><option value="DROP">DROP — drop requests</option></select></L>
        {!baseline && <L t="Affected requests (%)" hint="Share of gateway requests that get the fault (0–100)."><input className="inp" type="number" min={0} max={100} value={pctV} onChange={(e) => setPct(+e.target.value)} /></L>}
        {fault === "LATENCY" && <L t="Injected latency (ms)"><input className="inp" type="number" min={0} value={lat} onChange={(e) => setLat(+e.target.value)} /></L>}
        {fault === "ERROR" && <L t="HTTP error code" hint="Status returned for affected requests."><input className="inp" type="number" min={100} max={599} value={code} onChange={(e) => setCode(+e.target.value)} /></L>}
        <L t="Duration (seconds)" hint="Send traffic through http://localhost:8081/api/** while it runs — only that traffic is measured."><input className="inp" type="number" min={1} value={dur} onChange={(e) => setDur(+e.target.value)} /></L>
        <button className="btn" disabled={bad || busy || !!active}><Play className="size-4" />{busy ? "Starting…" : "Start experiment"}</button></div></Card>
    </form>);
}
