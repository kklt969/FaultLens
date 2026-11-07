package com.faultlens.demo.Metrics.Service;

import com.faultlens.demo.Experiment.Model.Experiment;
import com.faultlens.demo.Metrics.Model.ExperimentMetrics;
import com.faultlens.demo.Metrics.Model.ExperimentResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MetricsServiceTest {

    MetricsService metricsService = new MetricsService();
    @Test
    public void calculateP50(){

        List<Long> latencies = List.of(10L, 20L, 30L, 40L, 50L);

        double p = metricsService.findPercentile(latencies, 0.5);

        Assertions.assertEquals(30,p);


    }

    @Test
    public void calculateResultWithZeroRequests() {
        Experiment experiment = new Experiment();
        experiment.setMetrics(new ExperimentMetrics());

        ExperimentResult result = metricsService.calculateResult(experiment);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getTotalRequest());
        Assertions.assertEquals(0, result.getSuccessfulRequest());
        Assertions.assertEquals(0, result.getFailedRequest());
        Assertions.assertEquals(0.0, result.getErrorRate());
        Assertions.assertNull(result.getP50());
        Assertions.assertNull(result.getP95());
        Assertions.assertNull(result.getP99());
    }

}
