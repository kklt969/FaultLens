package com.faultlens.demo.Report.Model;

import java.util.List;

public class ResilienceReport {

    private String summary;
    private List<String> observations;
    private List<String> hypotheses;
    private List<String> recommendations;
    private List<String> limitations;

    public ResilienceReport() {
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getObservations() {
        return observations;
    }

    public void setObservations(List<String> observations) {
        this.observations = observations;
    }

    public List<String> getHypotheses() {
        return hypotheses;
    }

    public void setHypotheses(List<String> hypotheses) {
        this.hypotheses = hypotheses;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public List<String> getLimitations() {
        return limitations;
    }

    public void setLimitations(List<String> limitations) {
        this.limitations = limitations;
    }
}
