package com.faultlens.demo.Report.Model;

import java.util.UUID;

public class JobSubmitResponse {

    private UUID JobID;
    private ReportJobStatus JobStatus;

    public UUID getJobID() {
        return JobID;
    }

    public void setJobID(UUID jobID) {
        JobID = jobID;
    }

    public ReportJobStatus getJobStatus() {
        return JobStatus;
    }

    public void setJobStatus(ReportJobStatus jobStatus) {
        JobStatus = jobStatus;
    }
}
