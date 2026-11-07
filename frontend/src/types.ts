export type FaultType = "LATENCY" | "ERROR" | "DROP";
export interface ExperimentResult { totalRequest: number; successfulRequest: number; failedRequest: number; p50: number | null; p95: number | null; p99: number | null; errorRate: number }
export interface Experiment { id: number; name: string; latencyMs: number; percentage: number; errorCode: number; durationSeconds: number; active: boolean; faultType: FaultType; createdAt: string; result: ExperimentResult | null; metrics?: { totalRequests: number; successfulRequests: number; failedRequests: number } | null }
export type NewExperiment = Pick<Experiment, "name" | "latencyMs" | "percentage" | "errorCode" | "durationSeconds" | "faultType"> & { active: true };
export interface ExperimentComparison { baseline: ExperimentResult; fault: ExperimentResult; p50LatencyIncrease: number | null; p95LatencyIncrease: number | null; p99LatencyIncrease: number | null; throughputDifference: number | null; errorRateDifference: number | null }
export type ReportJobStatus = "QUEUED" | "RUNNING" | "COMPLETED" | "FAILED";
export interface ResilienceReport { summary: string; observations: string[]; hypotheses: string[]; recommendations: string[]; limitations: string[] }
export interface ReportJob { id: string; status: ReportJobStatus; firstExperimentId: number; secondExperimentId: number; report: ResilienceReport | null; errorMessage: string | null }
export interface CreateReportJobRequest { firstExperimentId: number; secondExperimentId: number }
