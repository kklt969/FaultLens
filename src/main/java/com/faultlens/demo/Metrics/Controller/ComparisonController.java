package com.faultlens.demo.Metrics.Controller;

import com.faultlens.demo.Experiment.Repository.ExperimentRepository;
import com.faultlens.demo.Experiment.Service.ExperimentService;
import com.faultlens.demo.Metrics.Model.ExperimentComparison;
import com.faultlens.demo.Metrics.Model.ExperimentResult;
import com.faultlens.demo.Metrics.Repository.ExperimentResultRepository;
import com.faultlens.demo.Metrics.Service.comparisonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/experiments")
public class ComparisonController {

    private final comparisonService comparisonService;
    private final ExperimentRepository experimentRepository;


    public ComparisonController(com.faultlens.demo.Metrics.Service.comparisonService comparisonService, ExperimentResultRepository experimentResultRepository, ExperimentRepository experimentRepository) {
        this.comparisonService = comparisonService;
        this.experimentRepository = experimentRepository;



    }

    @GetMapping("/compare")
    public ResponseEntity<?> compareExperiments(
            @RequestParam Integer firstId,
            @RequestParam Integer secondId) {

        if (firstId.equals(secondId)) {
            return ResponseEntity.badRequest()
                    .body("Please select two different experiments.");
        }

        ExperimentResult firstResult = comparisonService.getExperimentResult(firstId);

        ExperimentResult secondResult =comparisonService.getExperimentResult(secondId);


        if (firstResult == null || secondResult == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("One or both experiments do not have saved results.");
        }

        Optional<Integer> firstDurationOptional = experimentRepository.findDurationByExperimentId(firstId);


        Optional<Integer> secondDurationOptional = experimentRepository.findDurationByExperimentId(secondId);

        if (firstDurationOptional.isEmpty() || secondDurationOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("One or both experiments were not found.");
        }

        Integer firstDuration = firstDurationOptional.get();
        Integer secondDuration = secondDurationOptional.get();


        ExperimentComparison comparison =
                comparisonService.compareExperiments(
                        firstResult,
                        firstDuration,
                        secondResult,
                        secondDuration
                );

        return ResponseEntity.ok(comparison);
    }
}





