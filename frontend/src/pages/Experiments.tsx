import { useState } from "react";
import { Link } from "react-router-dom";
import { RefreshCw, Search } from "lucide-react";
import { useExperiments, statusOf } from "../hooks";
import { Badge, Card, Empty, ErrorBox, Skeleton, when } from "../components/ui";
const PAGE = 10;
export default function Experiments() {
  const { list, active, loading, error, refresh } = useExperiments(5000);
  const [q, setQ] = useState(""); const [fault, setFault] = useState(""); const [st, setSt] = useState(""); const [asc, setAsc] = useState(false); const [page, setPage] = useState(0);
  const rows = list.filter((e) => e.name?.toLowerCase().includes(q.toLowerCase()) && (!fault || e.faultType === fault) && (!st || statusOf(e, active?.id) === st)).sort((a, b) => (asc ? a.id - b.id : b.id - a.id));
  const pages = Math.max(1, Math.ceil(rows.length / PAGE)), cur = Math.min(page, pages - 1);
  return (<>
    {error && <ErrorBox msg={error} retry={refresh} />}
    <Card title="All experiments" right={<button className="btn ghost" onClick={refresh} aria-label="Refresh"><RefreshCw className="size-4" /></button>}>
      <div className="mb-4 flex flex-wrap gap-2">
        <div className="relative min-w-48 flex-1"><Search className="absolute left-3 top-2.5 size-4 text-mut" /><input className="inp pl-9" placeholder="Search by name" value={q} onChange={(e) => { setQ(e.target.value); setPage(0); }} /></div>
        <select className="inp w-auto" value={fault} onChange={(e) => { setFault(e.target.value); setPage(0); }}><option value="">All faults</option><option>LATENCY</option><option>ERROR</option><option>DROP</option></select>
        <select className="inp w-auto" value={st} onChange={(e) => { setSt(e.target.value); setPage(0); }}><option value="">All statuses</option><option>Running</option><option>Completed</option><option>Interrupted</option><option>Ended</option></select></div>
      {loading ? <Skeleton /> : !rows.length ? <Empty text="No experiments match." /> : <div className="overflow-x-auto"><table className="w-full text-left text-sm"><thead className="text-xs text-mut"><tr>
        <th className="py-2"><button onClick={() => setAsc(!asc)}>ID {asc ? "↑" : "↓"}</button></th><th>Name</th><th>Fault</th><th>Fault %</th><th>Duration</th><th>Status</th><th>Created</th></tr></thead><tbody>
        {rows.slice(cur * PAGE, cur * PAGE + PAGE).map((e) => <tr key={e.id} className="border-t border-line"><td className="py-2">#{e.id}</td><td><Link className="hover:text-brand" to={`/experiments/${e.id}`}>{e.name}</Link></td><td>{e.faultType}</td><td>{e.percentage}%</td><td>{e.durationSeconds}s</td><td><Badge s={statusOf(e, active?.id)} /></td><td className="text-mut">{when(e.createdAt)}</td></tr>)}</tbody></table></div>}
      {pages > 1 && <div className="mt-4 flex items-center justify-end gap-2 text-sm"><button className="btn ghost" disabled={cur === 0} onClick={() => setPage(cur - 1)}>Prev</button><span className="text-mut">{cur + 1} / {pages}</span><button className="btn ghost" disabled={cur >= pages - 1} onClick={() => setPage(cur + 1)}>Next</button></div>}
    </Card></>);
}
