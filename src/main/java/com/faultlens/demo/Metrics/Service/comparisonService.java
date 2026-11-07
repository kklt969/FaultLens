package com.faultlens.demo.Metrics.Service;

import com.faultlens.demo.Experiment.Model.Experiment;
import com.faultlens.demo.Experiment.Repository.ExperimentRepository;
import com.faultlens.demo.Metrics.Model.ExperimentComparison;
import com.faultlens.demo.Metrics.Model.ExperimentResult;
import com.faultlens.demo.Metrics.Repository.ExperimentResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class comparisonService {

    private ExperimentResultRepository experimentResultRepository;

    public comparisonService() {

    }

    @Autowired
   public comparisonService(ExperimentResultRepository experimentResultRepository) {
       this.experimentResultRepository = experimentResultRepository;
   }

    public ExperimentResult getExperimentResult(Integer experimentId) {

        if(experimentId == null) {
            return null;
        }

       ExperimentResult experimentResult = experimentResultRepository.findByExperimentId(experimentId)
               .stream().findFirst().orElse(null);

        return experimentResult;



    }

    public ExperimentComparison compareExperiments(
            ExperimentResult baseline,
            Integer baselineDurationSeconds,
            ExperimentResult faultInjected,
            Integer faultDurationSeconds) {

        if (baseline == null || faultInjected == null) {
            throw new IllegalArgumentException(
                    "Both baseline and fault-injected results are required"
            );
        }

        if (baselineDurationSeconds <= 0 || faultDurationSeconds <= 0) {
            throw new IllegalArgumentException(
                    "Experiment durations must be greater than zero"
            );
        }

        ExperimentComparison comparison = new ExperimentComparison();

        comparison.setBaseline(baseline);
        comparison.setFault(faultInjected);

        // Latency percentage changes
        comparison.setP50LatencyIncrease(
                calculatePercentageChange(
                        baseline.getP50(),
                        faultInjected.getP50()
                )
        );

        comparison.setP95LatencyIncrease(
                calculatePercentageChange(
                        baseline.getP95(),
                        faultInjected.getP95()
                )
        );

        comparison.setP99LatencyIncrease(
                calculatePercentageChange(
                        baseline.getP99(),
                        faultInjected.getP99()
                )
        );

        // Throughput = requests per second
        double baselineThroughput =
                (double) baseline.getTotalRequest()
                        / baselineDurationSeconds;

        double faultThroughput =
                (double) faultInjected.getTotalRequest()
                        / faultDurationSeconds;

        comparison.setThroughputDifference(
                calculatePercentageChange(
                        baselineThroughput,
                        faultThroughput
                )
        );

        // Absolute error-rate difference, not relative percentage change
        comparison.setErrorRateDifference(
                calculateDifference(
                        baseline.getErrorRate(),
                        faultInjected.getErrorRate()
                )
        );

        return comparison;
    }

    private Double calculatePercentageChange(
            Double baseline,
            Double injected) {

        // Cannot compare a metric that is missing from either result
        if (baseline == null || injected == null) {
            return null;
        }

        // Avoid division by zero
        if (baseline == 0.0) {
            // Both values are zero, so there is no change
            if (injected == 0.0) {
                return 0.0;
            }

            // Percentage change is undefined when baseline is zero
            return null;
        }

        return ((injected - baseline) / baseline) * 100.0;
    }

    private Double calculateDifference(
            Double baseline,
            Double injected) {

        if (baseline == null || injected == null) {
            return null;
        }

        return injected - baseline;
    }
}

