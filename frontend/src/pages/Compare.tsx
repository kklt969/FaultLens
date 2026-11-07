import { useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { Bar, BarChart, CartesianGrid, Legend, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";
import { Sparkles } from "lucide-react";
import { api, errMsg } from "../api";
import { useExperiments } from "../hooks";
import type { ExperimentComparison } from "../types";
import { Card, Empty, ErrorBox, f1, pct, tip } from "../components/ui";
export default function Compare() {
  const { list } = useExperiments(); const [sp] = useSearchParams();
  const [a, setA] = useState(sp.get("first") ?? ""); const [b, setB] = useState(sp.get("second") ?? "");
  const [cmp, setCmp] = useState<ExperimentComparison | null>(null); const [err, setErr] = useState<string | null>(null); const [busy, setBusy] = useState(false);
  const opts = list.filter((e) => e.result);
  async function run() { setBusy(true); setErr(null); setCmp(null); try { setCmp(await api.compare(+a, +b)); } catch (e) { setErr(errMsg(e)); } finally { setBusy(false); } }
  const Sel = ({ v, set, label }: { v: string; set: (s: string) => void; label: string }) => (
    <label className="flex-1 space-y-1 text-sm"><span>{label}</span><select className="inp" value={v} onChange={(e) => set(e.target.value)}><option value="">Select…</option>{opts.map((e) => <option key={e.id} value={e.id}>#{e.id} {e.name} ({e.faultType} {e.percentage}%)</option>)}</select></label>);
  // worse = true when change is bad for reliability
  const rows: [string, string, string, number | null, boolean][] = cmp ? [
    ["p50 latency (ms)", f1(cmp.baseline.p50), f1(cmp.fault.p50), cmp.p50LatencyIncrease, (cmp.p50LatencyIncrease ?? 0) > 0],
    ["p95 latency (ms)", f1(cmp.baseline.p95), f1(cmp.fault.p95), cmp.p95LatencyIncrease, (cmp.p95LatencyIncrease ?? 0) > 0],
    ["p99 latency (ms)", f1(cmp.baseline.p99), f1(cmp.fault.p99), cmp.p99LatencyIncrease, (cmp.p99LatencyIncrease ?? 0) > 0],
    ["Throughput (req/s change)", "—", "—", cmp.throughputDifference, (cmp.throughputDifference ?? 0) < 0],
    ["Error rate (pp difference)", `${f1(cmp.baseline.errorRate)}%`, `${f1(cmp.fault.errorRate)}%`, cmp.errorRateDifference, (cmp.errorRateDifference ?? 0) > 0]] : [];
  return (<>
    <Card title="Select experiments"><div className="flex flex-wrap items-end gap-4"><Sel v={a} set={setA} label="Baseline (first)" /><Sel v={b} set={setB} label="Fault-injected (second)" />
      <button className="btn" disabled={!a || !b || a === b || busy} onClick={run}>{busy ? "Comparing…" : "Compare"}</button></div>
      {a && a === b && <p className="mt-2 text-sm text-warn">Pick two different experiments.</p>}{!opts.length && <p className="mt-2 text-sm text-mut">Only experiments with saved results can be compared.</p>}</Card>
    {err && <ErrorBox msg={err} />}
    {cmp ? <>
      <Card title="Metric comparison (values computed by the backend)"><div className="overflow-x-auto"><table className="w-full text-left text-sm"><thead className="text-xs text-mut"><tr><th className="py-2">Metric</th><th>Baseline #{a}</th><th>Fault #{b}</th><th>Change</th></tr></thead><tbody>
        {rows.map(([m, x, y, d, worse]) => <tr key={m} className="border-t border-line"><td className="py-2">{m}</td><td>{x}</td><td>{y}</td><td className={d == null ? "text-mut" : d === 0 ? "" : worse ? "text-bad" : "text-ok"}>{m.startsWith("Error") ? (d == null ? "—" : `${d > 0 ? "+" : ""}${d.toFixed(1)} pp`) : pct(d)}</td></tr>)}</tbody></table></div></Card>
      <Card title="Latency percentiles (ms)"><div className="h-64"><ResponsiveContainer><BarChart data={(["p50", "p95", "p99"] as const).map((k) => ({ n: k, Baseline: cmp.baseline[k], Fault: cmp.fault[k] }))}><CartesianGrid stroke="#252E3D" vertical={false} /><XAxis dataKey="n" stroke="#94A3B8" /><YAxis stroke="#94A3B8" /><Tooltip {...tip} /><Legend /><Bar dataKey="Baseline" fill="#3B82F6" /><Bar dataKey="Fault" fill="#EF4444" /></BarChart></ResponsiveContainer></div></Card>
      <Link className="btn w-fit" to={`/reports?first=${a}&second=${b}`}><Sparkles className="size-4" />Generate AI report for this pair</Link></>
      : !err && <Empty text="Choose a baseline and a fault-injected experiment to compare." />}
  </>);
}
