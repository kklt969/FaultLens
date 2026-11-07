package com.faultlens.demo.Report.Service;

import com.faultlens.demo.Experiment.Model.Experiment;
import com.faultlens.demo.Experiment.Service.ExperimentService;
import com.faultlens.demo.Metrics.Model.ExperimentComparison;
import com.faultlens.demo.Metrics.Model.ExperimentResult;
import com.faultlens.demo.Metrics.Repository.ExperimentResultRepository;
import com.faultlens.demo.Metrics.Service.MetricsService;
import com.faultlens.demo.Metrics.Service.comparisonService;
import com.faultlens.demo.Report.Model.ExperimentContext;
import com.faultlens.demo.Report.Model.ResilienceAnalysisRequest;
import com.faultlens.demo.Report.Model.ResilienceReport;
import com.faultlens.demo.Report.Model.SystemContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ResilienceReportService {



    private final ExperimentService experimentService;
    private final comparisonService  comparisonService;
    private final MetricsService metricsService;
    private final ExperimentResultRepository experimentResultRepository;
    private final OllamaClient ollamaClient;

    public ResilienceReportService(comparisonService comparisonService, ExperimentService experimentService, MetricsService metricsService, ExperimentResultRepository experimentResultRepository, OllamaClient ollamaClient) {
        this.comparisonService = comparisonService;
        this.experimentService = experimentService;
        this.metricsService = metricsService;
        this.experimentResultRepository = experimentResultRepository;
        this.ollamaClient = ollamaClient;
    }

    public ResilienceReport generateReport(
            Integer firstId,
            Integer secondId) {

        // 1. Retrieve both saved experiment results.


        ExperimentResult result1 = experimentResultRepository.findByExperiment_Id(firstId).orElse(null);
        ExperimentResult result2 = experimentResultRepository.findByExperiment_Id(secondId).orElse(null);



        // 2. Retrieve their configurations.

        Experiment experiment1 = experimentService.getExperimentById(firstId);
        Experiment experiment2 = experimentService.getExperimentById(secondId);

        //Validate Experiment and Result
        if (result1 == null || result2 == null) {
            throw new IllegalArgumentException(
                    "Both experiments must have saved results."
            );
        }

        if (experiment1 == null || experiment2 == null) {
            throw new IllegalArgumentException(
                    "One or both experiments were not found."
            );
        }

        // 3. Calculate their comparison.

        ExperimentComparison comparison = comparisonService.compareExperiments(result1,experiment1.getDurationSeconds(),result2,experiment2.getDurationSeconds());

        // 4. Build the LLM request.


        ResilienceAnalysisRequest analysisRequest = new ResilienceAnalysisRequest();
        analysisRequest.setComparison(comparison);

        ExperimentContext experimentContext1 = getExperimentContext(experiment1);
        ExperimentContext experimentContext2 = getExperimentContext(experiment2);

        List<ExperimentContext> contexts = new ArrayList<>();
        contexts.add(experimentContext1);
        contexts.add(experimentContext2);

        analysisRequest.setExperiments(contexts);
        analysisRequest.setSystemContext(getSystemContext());


        // 5. Validate and return the report.

        ResilienceReport resilienceReport = ollamaClient.generateReport(analysisRequest);
        return resilienceReport;



    }

    private ExperimentContext getExperimentContext(Experiment experiment) {
        ExperimentContext experimentContext = new ExperimentContext();
        experimentContext.setName(experiment.getName());
        experimentContext.setLatencyMs(experiment.getLatencyMs());
        experimentContext.setDurationSeconds(experiment.getDurationSeconds());
        experimentContext.setFaultPercentage(experiment.getPercentage());
        experimentContext.setFaultType(experiment.getFaultType());
        return experimentContext;
    }

    private SystemContext getSystemContext(){
        SystemContext systemContext = new SystemContext();
        systemContext.setSystemName("RateFlow");
        systemContext.setComponents(List.of(
                "Spring Boot REST API",
                "Redis"
        ));
        systemContext.setDependencies(List.of(
                "Redis"
        ));
        systemContext.setCommunicationPaths(List.of(
                "HTTP client -> RateFlow API",
                "RateFlow API -> Redis"
        ));
    return systemContext;

    }





}
