package com.faultlens.demo.Metrics.Service;

import com.faultlens.demo.Metrics.Model.ExperimentComparison;
import com.faultlens.demo.Metrics.Model.ExperimentResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComparisonServiceTest {
    private comparisonService comparisonService;

    @BeforeEach
    void setUp() {
        comparisonService = new comparisonService();
    }

    @Test
    void shouldCalculateLatencyIncreaseAndThroughputDifference() {
        ExperimentResult baseline = createResult(
                100.0, 200.0, 300.0, 100, 5.0, 6000
        );

        ExperimentResult fault = createResult(
                150.0, 300.0, 450.0, 100, 10.0, 4200
        );

        ExperimentComparison result =
                comparisonService.compareExperiments(
                        baseline, 60, fault, 60
                );

        assertEquals(50.0, result.getP50LatencyIncrease(), 0.001);
        assertEquals(50.0, result.getP95LatencyIncrease(), 0.001);
        assertEquals(50.0, result.getP99LatencyIncrease(), 0.001);

        // Baseline: 6000 / 60 = 100 RPS
        // Fault:    4200 / 60 = 70 RPS
        assertEquals(-30.0, result.getThroughputDifference(), 0.001);

        // Absolute percentage-point difference: 10 - 5 = +5
        assertEquals(5.0, result.getErrorRateDifference(), 0.001);
    }

    @Test
    void shouldReturnZeroWhenBothThroughputsAreZero() {
        ExperimentResult baseline = createResult(
                100.0, 200.0, 300.0, 0, 0.0, 0
        );

        ExperimentResult fault = createResult(
                150.0, 300.0, 450.0, 0, 0.0, 0
        );

        ExperimentComparison result =
                comparisonService.compareExperiments(
                        baseline, 60, fault, 60
                );

        assertEquals(0.0, result.getThroughputDifference(), 0.001);
    }

    @Test
    void shouldReturnNullWhenBaselineThroughputIsZeroButFaultIsPositive() {
        ExperimentResult baseline = createResult(
                100.0, 200.0, 300.0, 0, 0.0, 0
        );

        ExperimentResult fault = createResult(
                150.0, 300.0, 450.0, 0, 0.0, 300
        );

        ExperimentComparison result =
                comparisonService.compareExperiments(
                        baseline, 60, fault, 60
                );

        assertNull(result.getThroughputDifference());
    }

    @Test
    void shouldReturnNullForMissingLatencyPercentiles() {
        ExperimentResult baseline = createResult(
                null, null, null, 10, 0.0, 600
        );

        ExperimentResult fault = createResult(
                150.0, 300.0, 450.0, 10, 0.0, 600
        );

        ExperimentComparison result =
                comparisonService.compareExperiments(
                        baseline, 60, fault, 60
                );

        assertNull(result.getP50LatencyIncrease());
        assertNull(result.getP95LatencyIncrease());
        assertNull(result.getP99LatencyIncrease());
    }

    @Test
    void shouldReturnNullWhenBothLatencyPercentilesAreMissing() {
        ExperimentResult baseline = createResult(
                null, null, null, 0, 0.0, 0
        );

        ExperimentResult fault = createResult(
                null, null, null, 0, 0.0, 0
        );

        ExperimentComparison result =
                comparisonService.compareExperiments(
                        baseline, 60, fault, 60
                );

        assertNull(result.getP50LatencyIncrease());
        assertNull(result.getP95LatencyIncrease());
        assertNull(result.getP99LatencyIncrease());
    }

    @Test
    void shouldRejectNullResults() {
        ExperimentResult valid = createResult(
                100.0, 200.0, 300.0, 10, 0.0, 600
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> comparisonService.compareExperiments(
                        null, 60, valid, 60
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> comparisonService.compareExperiments(
                        valid, 60, null, 60
                )
        );
    }

    @Test
    void shouldRejectInvalidDurations() {
        ExperimentResult valid = createResult(
                100.0, 200.0, 300.0, 10, 0.0, 600
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> comparisonService.compareExperiments(
                        valid, 0, valid, 60
                )
        );
    }

    private ExperimentResult createResult(
            Double p50,
            Double p95,
            Double p99,
            int failedRequests,
            Double errorRate,
            int totalRequests) {

        ExperimentResult result = new ExperimentResult();

        result.setP50(p50);
        result.setP95(p95);
        result.setP99(p99);
        result.setTotalRequest(totalRequests);
        result.setSuccessfulRequest(totalRequests - failedRequests);
        result.setFailedRequest(failedRequests);
        result.setErrorRate(errorRate);

        return result;
    }
}
