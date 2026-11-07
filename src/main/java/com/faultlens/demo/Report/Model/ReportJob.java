package com.faultlens.demo.Report.Model;

import java.util.UUID;

public class ReportJob {

    private UUID id;
    private volatile ReportJobStatus status;

    private Integer firstExperimentId;
    private Integer secondExperimentId;

    private volatile ResilienceReport report; // visibility of updates between threads, without it changes made in one thread might not immediately update main memory.
    private volatile String errorMessage;


    public ReportJob(UUID id, ReportJobStatus status, Integer firstExperimentId, Integer secondExperimentId, ResilienceReport report, String errorMessage) {
        this.id = id;
        this.status = status;
        this.firstExperimentId = firstExperimentId;
        this.secondExperimentId = secondExperimentId;
        this.report = report;
        this.errorMessage = errorMessage;
    }

    public ReportJob(){

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ReportJobStatus getStatus() {
        return status;
    }

    public void setStatus(ReportJobStatus status) {
        this.status = status;
    }

    public Integer getFirstExperimentId() {
        return firstExperimentId;
    }

    public void setFirstExperimentId(Integer firstExperimentId) {
        this.firstExperimentId = firstExperimentId;
    }

    public Integer getSecondExperimentId() {
        return secondExperimentId;
    }

    public void setSecondExperimentId(Integer secondExperimentId) {
        this.secondExperimentId = secondExperimentId;
    }

    public ResilienceReport getReport() {
        return report;
    }

    public void setReport(ResilienceReport report) {
        this.report = report;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
