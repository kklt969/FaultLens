package com.faultlens.demo.Metrics.Model;

import com.faultlens.demo.Experiment.Model.Experiment;
import jakarta.persistence.*;

@Entity
public class ExperimentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer resultId;

    @OneToOne
    @JoinColumn(name = "experiment_id", nullable = false, unique = true)
    private Experiment experiment;



    private long totalRequest;
    private long successfulRequest;
    private long failedRequest;
    private Double p50;
    private Double p95;
    private Double p99;

    private double errorRate;


    public ExperimentResult(long totalRequest, long successfulRequest, long failedRequest, Double p50, Double p95, Double p99, Double errorRate) {

        this.totalRequest = totalRequest;
        this.successfulRequest = successfulRequest;
        this.failedRequest = failedRequest;
        this.p50 = p50;
        this.p95 = p95;
        this.p99 = p99;
        this.errorRate = errorRate;

    }
    public ExperimentResult() {
    }


    public long getTotalRequest() {
        return totalRequest;
    }


    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public long getSuccessfulRequest() {
        return successfulRequest;
    }

    public long getFailedRequest() {
        return failedRequest;
    }

    public Double getP50() {
        return p50;
    }

    public Double getP95() {
        return p95;
    }

    public Double getP99() {
        return p99;
    }

    public void setTotalRequest(long totalRequest) {
        this.totalRequest = totalRequest;
    }

    public void setSuccessfulRequest(long successfulRequest) {
        this.successfulRequest = successfulRequest;
    }

    public void setFailedRequest(long failedRequest) {
        this.failedRequest = failedRequest;
    }

    public void setP50(Double p50) {
        this.p50 = p50;
    }

    public void setP95(Double p95) {
        this.p95 = p95;
    }

    public void setP99(Double p99) {
        this.p99 = p99;
    }

    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
    }

    public double getErrorRate() {
        return errorRate;
    }

    @Override
    public String toString() {
        return "ExperimentResult{" +
                "totalRequests=" + totalRequest +
                ", successfulRequests=" + successfulRequest +
                ", failedRequests=" + failedRequest +
                ", p50=" + p50 +
                ", p95=" + p95 +
                ", p99=" + p99 +
                ", errorRate=" + errorRate +
                '}';
    }
}
