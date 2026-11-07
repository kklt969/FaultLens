package com.faultlens.demo.Experiment.Model;

import com.faultlens.demo.Metrics.Model.ExperimentMetrics;
import com.faultlens.demo.Metrics.Model.ExperimentResult;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Experiment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;

    private long latencyMs;
    private int percentage;
    private int errorCode;
    private int durationSeconds;

    @Transient // tells JPA to ignore metrics, use it for in-memory run time
    private ExperimentMetrics metrics;

    @OneToOne(mappedBy = "experiment")
    private ExperimentResult result;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Experiment(String name, long latencyMs, int percentage, int errorCode, boolean active, FaultType faultType,int durationSeconds) {

        this.name = name;
        this.latencyMs = latencyMs;
        this.percentage = percentage;
        this.errorCode = errorCode;
        this.active = active;
        this.faultType = faultType;
        this.durationSeconds = durationSeconds;
    }
    public Experiment() {
    }
    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
    public int getDurationSeconds() {
        return durationSeconds;
    }

    public ExperimentMetrics getMetrics() {
        return metrics;
    }

    public void setMetrics(ExperimentMetrics metrics) {
        this.metrics = metrics;
    }

    public long getLatencyMs() {
        return latencyMs;
    }

    public void setLatencyMs(long latencyMs) {
        this.latencyMs = latencyMs;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    private boolean active;

    @Enumerated(EnumType.STRING)
    private FaultType faultType;


    public ExperimentResult getResult() {
        return result;
    }

    public void setResult(ExperimentResult result) {
        this.result = result;
    }

    public Integer getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public FaultType getFaultType() {
        return faultType;
    }

    public void setFaultType(FaultType faultType) {
        this.faultType = faultType;
    }

    @Override
    public String toString() {
        return "Experiment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latencyMs=" + latencyMs +
                ", percentage=" + percentage +
                ", errorCode=" + errorCode +
                ", active=" + active +
                ", faultType=" + faultType +
                '}';
    }
}


