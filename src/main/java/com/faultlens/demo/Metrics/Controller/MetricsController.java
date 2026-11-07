package com.faultlens.demo.Metrics.Controller;

import com.faultlens.demo.Experiment.Model.Experiment;
import com.faultlens.demo.Experiment.Service.ExperimentService;
import com.faultlens.demo.Metrics.Model.ExperimentResult;
import com.faultlens.demo.Metrics.Service.MetricsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetricsController {


    private final MetricsService metricsService;
    private final ExperimentService experimentService;

    public MetricsController(MetricsService metricsService, ExperimentService experimentService) {
        this.metricsService = metricsService;
        this.experimentService = experimentService;
    }

    @GetMapping("/experiments/result")
    public ResponseEntity<ExperimentResult> getResult() {

        Experiment experiment = experimentService.getActiveExperiment();


        if (experiment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ExperimentResult result = metricsService.calculateResult(experiment);

        if(result == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);


    }




}
