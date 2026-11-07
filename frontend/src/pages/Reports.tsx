import { useState } from "react";
import { useSearchParams } from "react-router-dom";
import { CheckCircle2, Loader2, Sparkles, XCircle } from "lucide-react";
import { useExperiments, useReportJob } from "../hooks";
import { Card, ErrorBox } from "../components/ui";
const List = ({ title, items, tone, note }: { title: string; items: string[]; tone: string; note?: string }) => (
  <Card title={title} className={tone}>{note && <p className="mb-2 text-xs text-ai">{note}</p>}{items?.length ? <ul className="list-disc space-y-2 pl-5 text-sm">{items.map((t, i) => <li key={i}>{t}</li>)}</ul> : <p className="text-sm text-mut">None provided.</p>}</Card>);
export default function Reports() {
  const { list } = useExperiments(); const [sp] = useSearchParams(); const { job, error, busy, start } = useReportJob();
  const [a, setA] = useState(sp.get("first") ?? ""); const [b, setB] = useState(sp.get("second") ?? "");
  const opts = list.filter((e) => e.result);
  const Sel = ({ v, set, label }: { v: string; set: (s: string) => void; label: string }) => (
    <label className="flex-1 space-y-1 text-sm"><span>{label}</span><select className="inp" value={v} onChange={(e) => set(e.target.value)}><option value="">Select…</option>{opts.map((e) => <option key={e.id} value={e.id}>#{e.id} {e.name}</option>)}</select></label>);
  const r = job?.report;
  return (<>
    <Card title="Generate AI resilience report"><div className="flex flex-wrap items-end gap-4"><Sel v={a} set={setA} label="Baseline (first)" /><Sel v={b} set={setB} label="Fault-injected (second)" />
      <button className="btn bg-ai" disabled={!a || !b || a === b || busy} onClick={() => start(+a, +b)}><Sparkles className="size-4" />{busy ? "Generating…" : "Generate report"}</button></div>
      <p className="mt-3 text-xs text-mut">Generated locally by Ollama (llama3.2:3b) through the FaultLens backend. A small model can be wrong — verify before acting.</p></Card>
    {error && <ErrorBox msg={error} />}
    {job && <Card title={`Job ${job.id.slice(0, 8)}`}><div className="flex items-center gap-2 text-sm">
      {job.status === "COMPLETED" ? <CheckCircle2 className="size-4 text-ok" /> : job.status === "FAILED" ? <XCircle className="size-4 text-bad" /> : <Loader2 className="size-4 animate-spin text-ai" />}
      {job.status}{job.status === "QUEUED" && " — waiting for the worker"}{job.status === "RUNNING" && " — the model is writing the report (can take a minute)"}</div>
      {job.status === "FAILED" && <p className="mt-2 text-sm text-bad">{job.errorMessage ?? "Report generation failed."}</p>}</Card>}
    {r && <>
      <Card title="Summary" className="border-ai/40"><p className="text-sm leading-relaxed">{r.summary}</p></Card>
      <div className="grid gap-4 lg:grid-cols-2">
        <List title="Observations" items={r.observations} tone="border-ok/30" note="Findings meant to be grounded in the measured data." />
        <List title="Hypotheses" items={r.hypotheses} tone="border-dashed border-ai/50" note="Unconfirmed explanations — not established facts." />
        <List title="Recommendations" items={r.recommendations} tone="border-brand/30" /><List title="Limitations" items={r.limitations} tone="border-warn/30" /></div></>}
  </>);
}
