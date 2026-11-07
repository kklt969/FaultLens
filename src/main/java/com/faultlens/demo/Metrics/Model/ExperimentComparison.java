package com.faultlens.demo.Metrics.Model;


public class ExperimentComparison {

    private ExperimentResult baseline;
    private ExperimentResult fault;

    // Percentage changes; nullable when they cannot be calculated
    private Double p50LatencyIncrease;
    private Double p95LatencyIncrease;
    private Double p99LatencyIncrease;
    private Double throughputDifference;

    // Absolute difference in error rate, not relative percentage change
    private Double errorRateDifference;

    public ExperimentResult getBaseline() {
        return baseline;
    }

    public void setBaseline(ExperimentResult baseline) {
        this.baseline = baseline;
    }

    public ExperimentResult getFault() {
        return fault;
    }

    public void setFault(ExperimentResult fault) {
        this.fault = fault;
    }

    public Double getP50LatencyIncrease() {
        return p50LatencyIncrease;
    }

    public void setP50LatencyIncrease(Double p50LatencyIncrease) {
        this.p50LatencyIncrease = p50LatencyIncrease;
    }

    public Double getP95LatencyIncrease() {
        return p95LatencyIncrease;
    }

    public void setP95LatencyIncrease(Double p95LatencyIncrease) {
        this.p95LatencyIncrease = p95LatencyIncrease;
    }

    public Double getP99LatencyIncrease() {
        return p99LatencyIncrease;
    }

    public void setP99LatencyIncrease(Double p99LatencyIncrease) {
        this.p99LatencyIncrease = p99LatencyIncrease;
    }

    public Double getThroughputDifference() {
        return throughputDifference;
    }

    public void setThroughputDifference(Double throughputDifference) {
        this.throughputDifference = throughputDifference;
    }

    public Double getErrorRateDifference() {
        return errorRateDifference;
    }

    public void setErrorRateDifference(Double errorRateDifference) {
        this.errorRateDifference = errorRateDifference;
    }
}
