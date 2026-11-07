import { Link, useParams } from "react-router-dom";
import { Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";
import { useExperiments, statusOf } from "../hooks";
import { Badge, Card, ErrorBox, Skeleton, Stat, f1, tip, when } from "../components/ui";
import TrafficPanel from "../components/TrafficPanel";

export default function Details() {
  const id = Number(useParams().id); const { list, active, loading, error, refresh } = useExperiments(2000);
  const e = list.find((x) => x.id === id);
  if (loading) return <Skeleton />;
  if (error) return <ErrorBox msg={error} retry={refresh} />;
  if (!e) return <ErrorBox msg={`Experiment #${id} not found.`} />;
  const s = statusOf(e, active?.id), r = e.result, live = active?.id === e.id ? active.metrics : null;
  const cfg: [string, string][] = [["Fault type", e.faultType], ["Affected requests", `${e.percentage}%`], ["Duration", `${e.durationSeconds}s`], ["Latency", e.faultType === "LATENCY" ? `${e.latencyMs} ms` : "—"], ["Error code", e.faultType === "ERROR" ? String(e.errorCode) : "—"], ["Created", when(e.createdAt)]];
  return (<>
    <div className="flex flex-wrap items-center gap-3"><h2 className="text-lg font-semibold">#{e.id} {e.name}</h2><Badge s={s} />
      <Link to={`/comparisons?first=${e.id}`} className="btn ghost ml-auto">Compare</Link></div>
    <Card title="Configuration"><dl className="grid grid-cols-2 gap-4 text-sm md:grid-cols-3">{cfg.map(([k, v]) => <div key={k}><dt className="text-xs text-mut">{k}</dt><dd>{v}</dd></div>)}</dl></Card>
    {live && <div className="grid grid-cols-3 gap-4"><Stat label="Live requests" value={live.totalRequests} /><Stat label="Live successful" value={live.successfulRequests} /><Stat label="Live failed" value={live.failedRequests} /></div>}
    {s === "Running" && <TrafficPanel running />}
    {r ? <>
      <div className="grid grid-cols-2 gap-4 lg:grid-cols-4"><Stat label="Total requests" value={r.totalRequest.toLocaleString()} /><Stat label="Successful" value={r.successfulRequest.toLocaleString()} /><Stat label="Failed" value={r.failedRequest.toLocaleString()} /><Stat label="Error rate" value={`${f1(r.errorRate)}%`} /></div>
      <Card title="Latency percentiles (ms)"><div className="h-56"><ResponsiveContainer><BarChart data={[{ n: "p50", v: r.p50 }, { n: "p95", v: r.p95 }, { n: "p99", v: r.p99 }]}><CartesianGrid stroke="#252E3D" vertical={false} /><XAxis dataKey="n" stroke="#94A3B8" /><YAxis stroke="#94A3B8" /><Tooltip {...tip} /><Bar dataKey="v" name="ms" fill="#3B82F6" /></BarChart></ResponsiveContainer></div>
        {r.totalRequest === 0 && <p className="text-sm text-warn">No requests were recorded. Send traffic through localhost:8081/api/** during a run.</p>}</Card></>
      : <Card><p className="text-sm text-mut">{s === "Running" ? "Running — results appear when the duration elapses." : s === "Interrupted" ? "This run was superseded or the backend restarted before it finished, so no result was saved." : "No result saved."}</p></Card>}
  </>);
}
