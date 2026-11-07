package com.faultlens.demo.Metrics.Model;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ExperimentMetrics {

    private AtomicLong totalRequests;

    private AtomicLong successfulRequests;
    private AtomicLong failedRequests;



    private CopyOnWriteArrayList<Long> latencies;

    public ExperimentMetrics() {
        totalRequests = new AtomicLong(0);
        successfulRequests = new AtomicLong(0);
        failedRequests = new AtomicLong(0);
        latencies = new CopyOnWriteArrayList<>();
    }


    public void incrementTotalRequests() {
        totalRequests.incrementAndGet();
    }
    public void incrementSuccessfulRequests() {
        successfulRequests.incrementAndGet();
    }
    public void incrementFailedRequests() {
        failedRequests.incrementAndGet();
    }

    public void addLatency(long latency) {

        latencies.add(latency);
    }

    public long getTotalRequests() {

        return totalRequests.get();
    }

    public long getSuccessfulRequests() {
        return successfulRequests.get();
    }

    public long getFailedRequests() {
        return failedRequests.get();
    }

    public CopyOnWriteArrayList<Long> getLatencies() {
        return latencies;
    }
}
