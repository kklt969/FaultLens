import { Link } from "react-router-dom";
import { Bar, BarChart, CartesianGrid, Legend, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";
import { Plus, RefreshCw } from "lucide-react";
import { useExperiments, statusOf } from "../hooks";
import { Badge, Card, Empty, ErrorBox, Skeleton, Stat, tip, when } from "../components/ui";
export default function Dashboard() {
  const { list, active, loading, error, refresh } = useExperiments(5000);
  const done = list.filter((e) => e.result);
  const total = done.reduce((s, e) => s + e.result!.totalRequest, 0), failed = done.reduce((s, e) => s + e.result!.failedRequest, 0);
  const data = done.slice(0, 8).reverse().map((e) => ({ name: `#${e.id}`, p50: e.result!.p50, p95: e.result!.p95, p99: e.result!.p99, ok: e.result!.successfulRequest, failed: e.result!.failedRequest }));
  return (<>
    <div className="flex items-center justify-between"><p className="text-sm text-mut">Fault-injection experiments against your target service.</p>
      <div className="flex gap-2"><button className="btn ghost" onClick={refresh} aria-label="Refresh"><RefreshCw className="size-4" /></button><Link to="/create" className="btn"><Plus className="size-4" />Create experiment</Link></div></div>
    {error && <ErrorBox msg={error} retry={refresh} />}
    <div className="grid grid-cols-2 gap-4 lg:grid-cols-4">
      <Stat label="Total experiments" value={list.length} /><Stat label="Running" value={active ? 1 : 0} sub={active?.name} />
      <Stat label="Completed" value={done.length} />
      <Stat label="Request success rate" value={total ? `${(100 - (failed / total) * 100).toFixed(1)}%` : "—"} sub={total ? `${total.toLocaleString()} requests` : "No completed runs yet"} /></div>
    <div className="grid gap-4 lg:grid-cols-2">
      <Card title="Latency percentiles (ms), last 8 completed">{data.length ? <div className="h-64"><ResponsiveContainer><BarChart data={data}><CartesianGrid stroke="#252E3D" vertical={false} /><XAxis dataKey="name" stroke="#94A3B8" fontSize={12} /><YAxis stroke="#94A3B8" fontSize={12} /><Tooltip {...tip} /><Legend />
        <Bar dataKey="p50" fill="#3B82F6" /><Bar dataKey="p95" fill="#8B5CF6" /><Bar dataKey="p99" fill="#F59E0B" /></BarChart></ResponsiveContainer></div> : <Empty text="No completed experiments yet." />}</Card>
      <Card title="Successful vs failed requests">{data.length ? <div className="h-64"><ResponsiveContainer><BarChart data={data}><CartesianGrid stroke="#252E3D" vertical={false} /><XAxis dataKey="name" stroke="#94A3B8" fontSize={12} /><YAxis stroke="#94A3B8" fontSize={12} /><Tooltip {...tip} /><Legend />
        <Bar dataKey="ok" name="Successful" stackId="a" fill="#22C55E" /><Bar dataKey="failed" name="Failed" stackId="a" fill="#EF4444" /></BarChart></ResponsiveContainer></div> : <Empty text="No completed experiments yet." />}</Card></div>
    <Card title="Recent experiments" right={<Link to="/experiments" className="text-xs text-brand">View all</Link>}>
      {loading ? <Skeleton /> : !list.length ? <Empty text="No experiments yet." /> : <div className="overflow-x-auto"><table className="w-full text-left text-sm"><thead className="text-xs text-mut"><tr><th className="py-2">ID</th><th>Name</th><th>Fault</th><th>Status</th><th>Created</th></tr></thead><tbody>
        {list.slice(0, 5).map((e) => <tr key={e.id} className="border-t border-line"><td className="py-2">#{e.id}</td><td><Link className="hover:text-brand" to={`/experiments/${e.id}`}>{e.name}</Link></td><td>{e.faultType}</td><td><Badge s={statusOf(e, active?.id)} /></td><td className="text-mut">{when(e.createdAt)}</td></tr>)}</tbody></table></div>}</Card>
  </>);
}
