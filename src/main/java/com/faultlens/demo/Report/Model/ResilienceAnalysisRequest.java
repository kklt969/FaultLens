package com.faultlens.demo.Report.Model;

import com.faultlens.demo.Metrics.Model.ExperimentComparison;

import java.util.List;

public class ResilienceAnalysisRequest {
    private ExperimentComparison comparison;
    private List<ExperimentContext> experiments;
    private SystemContext systemContext;

    public ResilienceAnalysisRequest() {
    }

    public ExperimentComparison getComparison() {
        return comparison;
    }

    public void setComparison(ExperimentComparison comparison) {
        this.comparison = comparison;
    }

    public List<ExperimentContext> getExperiments() {
        return experiments;
    }

    public void setExperiments(List<ExperimentContext> experiments) {
        this.experiments = experiments;
    }

    public SystemContext getSystemContext() {
        return systemContext;
    }

    public void setSystemContext(SystemContext systemContext) {
        this.systemContext = systemContext;
    }

    public ResilienceAnalysisRequest(ExperimentComparison comparison, List<ExperimentContext> experiments, SystemContext systemContext) {
        this.comparison = comparison;
        this.experiments = experiments;
        this.systemContext = systemContext;
    }

    @Override
    public String toString() {
        return "ResilienceAnalysisRequest{" +
                "comparison=" + comparison +
                ", experiments=" + experiments +
                ", systemContext=" + systemContext +
                '}';
    }
}
