import { useState } from "react";
import { api, BASE, errMsg } from "../api";
import { Card } from "../components/ui";
const Row = ({ k, v }: { k: string; v: string }) => <div className="flex justify-between gap-4 border-t border-line py-2 text-sm first:border-0"><span className="text-mut">{k}</span><span>{v}</span></div>;
export default function Settings() {
  const [res, setRes] = useState<{ ok: boolean; msg: string } | null>(null); const [busy, setBusy] = useState(false);
  async function test() { setBusy(true); try { const l = await api.history(); setRes({ ok: true, msg: `Connected — ${l.length} experiments found.` }); } catch (e) { setRes({ ok: false, msg: errMsg(e) }); } finally { setBusy(false); } }
  return (<div className="mx-auto max-w-xl space-y-4">
    <Card title="Backend connection"><Row k="Backend URL" v={BASE || "Vite proxy → http://localhost:8081"} />
      <div className="mt-3 flex items-center gap-3"><button className="btn" onClick={test} disabled={busy}>{busy ? "Testing…" : "Test connection"}</button>{res && <span className={`text-sm ${res.ok ? "text-ok" : "text-bad"}`}>{res.msg}</span>}</div>
      <p className="mt-3 text-xs text-mut">Change it with VITE_API_BASE_URL in frontend/.env, then restart the dev server.</p></Card>
    <Card title="Target service (read-only)"><Row k="Service" v="RateFlow" /><Row k="Target URL" v="http://localhost:8080" /><Row k="Measured route" v="http://localhost:8081/api/**" />
      <p className="mt-3 text-xs text-mut">From the backend's application.yml. There is no endpoint to change it at runtime.</p></Card>
    <Card title="AI configuration (read-only)"><Row k="Provider" v="Ollama (local)" /><Row k="Model" v="llama3.2:3b" /><p className="mt-3 text-xs text-mut">Taken from OllamaClient in the backend source, not a live check. The browser never talks to Ollama.</p></Card>
  </div>);
}
