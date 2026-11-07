import axios from "axios";
import type { CreateReportJobRequest, Experiment, ExperimentComparison, NewExperiment, ReportJob } from "./types";
export const BASE: string = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081";
const http = axios.create({ baseURL: BASE, headers: { "Content-Type": "application/json" }, timeout: 15000 });
export function errMsg(e: unknown): string {
  if (!axios.isAxiosError(e)) return e instanceof Error ? e.message : "Unexpected error";
  if (!e.response) return `Cannot reach the backend at ${BASE || "the Vite proxy"}. Is Spring Boot running (and CORS enabled)?`;
  const { status, data } = e.response;
  const text = typeof data === "string" && data ? data : "";
  if (status === 400) return text || "Bad request.";
  if (status === 404) return text || "Not found.";
  if (status === 503) return "Service unavailable.";
  if (status >= 500) return text || "Backend error (500). For report submits this can mean the report queue is full.";
  return text || `Request failed (${status})`;
}
export const api = {
  history: () => http.get<Experiment[]>("/experiments/history").then((r) => r.data),
  active: () => http.get<Experiment | "">("/experiments").then((r) => r.data || null),
  create: (b: NewExperiment) => http.post<{ id: number; name: string; createdAt: string }>("/experiments", b).then((r) => r.data),
  compare: (firstId: number, secondId: number) => http.get<ExperimentComparison>("/experiments/compare", { params: { firstId, secondId } }).then((r) => r.data),
  submitReport: (b: CreateReportJobRequest) => http.post<ReportJob>("/experiments/reports/submit", b).then((r) => r.data),
  getReport: (id: string) => http.get<ReportJob>(`/experiments/reports/${id}`).then((r) => r.data),
};
