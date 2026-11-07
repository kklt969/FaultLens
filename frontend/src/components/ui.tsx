import type { ReactNode } from "react";
import { AlertTriangle, RefreshCw } from "lucide-react";
export const Card = ({ title, right, children, className = "" }: { title?: string; right?: ReactNode; children: ReactNode; className?: string }) => (
  <section className={`rounded-lg border border-line bg-card ${className}`}>
    {title && <header className="flex items-center justify-between border-b border-line px-4 py-3"><h2 className="text-sm font-medium">{title}</h2>{right}</header>}
    <div className="p-4">{children}</div>
  </section>);
export const Stat = ({ label, value, sub }: { label: string; value: ReactNode; sub?: string }) => (
  <div className="rounded-lg border border-line bg-card p-4"><div className="text-xs text-mut">{label}</div><div className="mt-1 text-2xl font-semibold tabular-nums">{value}</div>{sub && <div className="mt-1 text-xs text-mut">{sub}</div>}</div>);
const tone: Record<string, string> = { Running: "text-brand bg-brand/10", Completed: "text-ok bg-ok/10", Interrupted: "text-warn bg-warn/10", Ended: "text-mut bg-elev" };
export const Badge = ({ s }: { s: string }) => (
  <span className={`inline-flex items-center gap-1.5 rounded px-2 py-0.5 text-xs ${tone[s]}`}><i className={`size-1.5 rounded-full bg-current ${s === "Running" ? "animate-pulse" : ""}`} />{s}</span>);
export const ErrorBox = ({ msg, retry }: { msg: string; retry?: () => void }) => (
  <div role="alert" className="flex items-start gap-3 rounded-lg border border-bad/40 bg-bad/10 p-4 text-sm"><AlertTriangle className="mt-0.5 size-4 shrink-0 text-bad" /><span className="flex-1">{msg}</span>{retry && <button className="btn ghost" onClick={retry}><RefreshCw className="size-3.5" />Retry</button>}</div>);
export const Skeleton = () => <div className="space-y-3">{[0, 1, 2].map((i) => <div key={i} className="h-10 animate-pulse rounded bg-elev" />)}</div>;
export const Empty = ({ text }: { text: string }) => <p className="py-8 text-center text-sm text-mut">{text}</p>;
export const f1 = (n: number | null | undefined) => (n == null ? "—" : n.toFixed(1));
export const pct = (n: number | null | undefined) => (n == null ? "—" : `${n > 0 ? "+" : ""}${n.toFixed(1)}%`);
export const when = (s: string) => new Date(s).toLocaleString();
export const tip = { contentStyle: { background: "#171E2A", border: "1px solid #252E3D", borderRadius: 6, fontSize: 12 }, cursor: { fill: "#ffffff08" } };
