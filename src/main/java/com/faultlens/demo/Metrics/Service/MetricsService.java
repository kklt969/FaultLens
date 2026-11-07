package com.faultlens.demo.Metrics.Service;

import com.faultlens.demo.Experiment.Model.Experiment;
import com.faultlens.demo.Metrics.Model.ExperimentMetrics;
import com.faultlens.demo.Metrics.Model.ExperimentResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class MetricsService {

    public ExperimentResult calculateResult(Experiment experiment) {

        ExperimentMetrics metrics = experiment.getMetrics();

        long total = metrics.getTotalRequests();
        long successful = metrics.getSuccessfulRequests();
        long failed = metrics.getFailedRequests();

       // int duration = metrics.;

        double errorRate = total == 0
                ? 0.0
                : (double) failed / total * 100.0;

        if (total == 0) {
            return new ExperimentResult(
                    total, successful, failed,
                    null, null, null, errorRate
            );
        }

        HashMap<String, Double> percentiles = calculatePercentile(experiment);

        return new ExperimentResult(
                total, successful, failed,
                percentiles.get("p50"),
                percentiles.get("p95"),
                percentiles.get("p99"),
                errorRate
        );


    }


    public HashMap<String,Double> calculatePercentile(Experiment experiment) {

        HashMap<String, Double> percentiles = new HashMap<>();

        List<Long> sortedLatencies = new ArrayList<>(experiment.getMetrics().getLatencies());
        Collections.sort(sortedLatencies);

        double p50Percentile = findPercentile(sortedLatencies,0.50);
       // double p75Percentile = findPercentile(sortedLatencies,0.75);
        double p95Percentile = findPercentile(sortedLatencies,0.95);

       // double p98Percentile = findPercentile(sortedLatencies,0.98);
        double p99Percentile = findPercentile(sortedLatencies,0.99);

        percentiles.put("p50", p50Percentile);
        //percentiles.put("p75", p75Percentile); // use if needed
        percentiles.put("p95", p95Percentile);
        //percentiles.put("p98", p98Percentile);
        percentiles.put("p99", p99Percentile);

        return percentiles;

    }


    public double findPercentile(List<Long> latencies, double percentile) {

        if(latencies==null || latencies.size()==0){
            throw new IllegalArgumentException("The list of latencies is null or empty");
        }
        if (percentile < 0 || percentile > 1) {
            throw new IllegalArgumentException("Percentile must be between 0 and 1");
        }

        int n = latencies.size();
        double rank = percentile*(n-1);

        int index = (int) Math.floor(rank);
        double fractionalPart = rank - index;

        // If it lands perfectly on an exact index
        if (index >= n - 1) {
            return latencies.get(n - 1);
        }

        // Linear interpolation between the two surrounding data points
        double lowerValue = latencies.get(index);
        double upperValue = latencies.get(index + 1);

        return lowerValue + fractionalPart * (upperValue - lowerValue);


    }


}
