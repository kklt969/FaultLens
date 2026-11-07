import { useEffect, useState } from "react";
import { NavLink, Outlet, useLocation } from "react-router-dom";
import { Activity, BarChart3, FlaskConical, GitCompare, LayoutDashboard, PanelLeft, PlusSquare, Settings, Sparkles } from "lucide-react";
import { api } from "../api";
const nav = [
  { to: "/", label: "Overview", icon: LayoutDashboard }, { to: "/experiments", label: "Experiments", icon: FlaskConical },
  { to: "/create", label: "Create Experiment", icon: PlusSquare }, { to: "/comparisons", label: "Comparisons", icon: GitCompare },
  { to: "/reports", label: "AI Reports", icon: Sparkles }, { to: "/settings", label: "Settings", icon: Settings }];
export default function Layout() {
  const [open, setOpen] = useState(window.innerWidth >= 768);
  const [up, setUp] = useState<boolean | null>(null);
  const { pathname } = useLocation();
  useEffect(() => { const ping = () => api.history().then(() => setUp(true), () => setUp(false)); ping(); const t = setInterval(ping, 15000); return () => clearInterval(t); }, []);
  const title = nav.find((n) => n.to === pathname)?.label ?? (pathname.startsWith("/experiments/") ? "Experiment details" : "FaultLens");
  return (
    <div className="flex min-h-screen">
      <aside className={`sticky top-0 h-screen shrink-0 border-r border-line bg-bg2 transition-[width] ${open ? "w-56" : "w-14"}`}>
        <div className="flex h-14 items-center gap-2 border-b border-line px-4"><Activity className="size-5 shrink-0 text-brand" />{open && <span className="font-semibold tracking-tight">FaultLens</span>}</div>
        <nav className="space-y-1 p-2">{nav.map(({ to, label, icon: I }) => (
          <NavLink key={to} to={to} end={to === "/"} title={label} className={({ isActive }) => `flex items-center gap-3 rounded-md px-2.5 py-2 text-sm ${isActive ? "bg-elev text-fg" : "text-mut hover:bg-elev/60 hover:text-fg"}`}>
            <I className="size-4 shrink-0" />{open && label}</NavLink>))}</nav>
      </aside>
      <div className="min-w-0 flex-1">
        <header className="flex h-14 items-center gap-3 border-b border-line bg-bg2 px-4">
          <button aria-label="Toggle sidebar" className="text-mut hover:text-fg" onClick={() => setOpen(!open)}><PanelLeft className="size-4" /></button>
          <h1 className="text-sm font-medium">{title}</h1>
          <div className="ml-auto flex items-center gap-2 text-xs text-mut"><i className={`size-2 rounded-full ${up === null ? "bg-mut" : up ? "bg-ok" : "bg-bad"}`} />{up === null ? "Checking…" : up ? "Backend connected" : "Backend unreachable"}</div>
        </header>
        <main className="mx-auto max-w-6xl space-y-6 p-4 md:p-6"><Outlet /></main>
      </div>
    </div>);
}
