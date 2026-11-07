package com.faultlens.demo.Report.Model;

import com.faultlens.demo.Experiment.Model.FaultType;

public class ExperimentContext {


    private String name;
    private FaultType faultType;
    private double faultPercentage;
    private long latencyMs;

    public ExperimentContext(String name, FaultType faultType, double faultPercentage, long latencyMs, int durationSeconds) {
        this.name = name;
        this.faultType = faultType;
        this.faultPercentage = faultPercentage;
        this.latencyMs = latencyMs;
        this.durationSeconds = durationSeconds;
    }

    private int durationSeconds;

    public ExperimentContext() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FaultType getFaultType() {
        return faultType;
    }

    public void setFaultType(FaultType faultType) {
        this.faultType = faultType;
    }

    public double getFaultPercentage() {
        return faultPercentage;
    }

    public void setFaultPercentage(double faultPercentage) {
        this.faultPercentage = faultPercentage;
    }

    public long getLatencyMs() {
        return latencyMs;
    }

    public void setLatencyMs(long latencyMs) {
        this.latencyMs = latencyMs;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    @Override
    public String toString() {
        return "ExperimentContext{" +
                "name='" + name + '\'' +
                ", faultType='" + faultType + '\'' +
                ", faultPercentage=" + faultPercentage +
                ", latencyMs=" + latencyMs +
                ", durationSeconds=" + durationSeconds +
                '}';
    }
}
