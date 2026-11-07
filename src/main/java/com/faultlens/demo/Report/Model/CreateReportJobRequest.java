package com.faultlens.demo.Report.Model;

public class CreateReportJobRequest {

    private Integer firstExperimentId;
    private Integer secondExperimentId;

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

}
