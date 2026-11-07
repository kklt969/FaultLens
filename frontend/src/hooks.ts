import { useCallback, useEffect, useRef, useState } from "react";
import { api, errMsg } from "./api";
import type { Experiment, ReportJob } from "./types";

export const statusOf = (e: Experiment, activeId?: number | null) =>
  e.id === activeId ? "Running" : e.result ? "Completed" : e.active ? "Interrupted" : "Ended";

export function useExperiments(pollMs = 0) {
  const [list, setList] = useState<Experiment[]>([]);
  const [active, setActive] = useState<Experiment | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const load = useCallback(async () => {
    try { const [l, a] = await Promise.all([api.history(), api.active()]); setList(l); setActive(a); setError(null); }
    catch (e) { setError(errMsg(e)); } finally { setLoading(false); }
  }, []);
  useEffect(() => { load(); if (!pollMs) return; const t = setInterval(load, pollMs); return () => clearInterval(t); }, [load, pollMs]);
  return { list, active, loading, error, refresh: load };
}

export function useReportJob() {
  const [job, setJob] = useState<ReportJob | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [busy, setBusy] = useState(false);
  const timer = useRef<number>(); const alive = useRef(true); const lock = useRef(false);
  useEffect(() => { alive.current = true; return () => { alive.current = false; clearTimeout(timer.current); }; }, []);
  const stop = () => { lock.current = false; setBusy(false); };
  const poll = useCallback((id: string, n: number) => {
    timer.current = window.setTimeout(async () => {
      try {
        const j = await api.getReport(id); if (!alive.current) return; setJob(j);
        if (j.status === "COMPLETED" || j.status === "FAILED") return stop();
        if (n >= 120) { setError("Timed out waiting for the report (5 min)."); return stop(); }
        poll(id, n + 1);
      } catch (e) { if (!alive.current) return; setError(errMsg(e)); stop(); }
    }, 2500);
  }, []);
  const start = useCallback(async (a: number, b: number) => {
    if (lock.current) return; lock.current = true; setBusy(true); setError(null); setJob(null);
    try { const j = await api.submitReport({ firstExperimentId: a, secondExperimentId: b }); setJob(j); poll(j.id, 0); }
    catch (e) { setError(errMsg(e)); stop(); }
  }, [poll]);
  return { job, error, busy, start };
}
