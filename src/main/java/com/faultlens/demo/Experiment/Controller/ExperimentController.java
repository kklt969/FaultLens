package com.faultlens.demo.Experiment.Controller;

import com.faultlens.demo.Experiment.Model.Experiment;
import com.faultlens.demo.Experiment.Model.ExperimentResponse;
import com.faultlens.demo.Experiment.Service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experiments")
public class ExperimentController {

      private final ExperimentService experimentService;

      @Autowired
      public ExperimentController(ExperimentService experimentService) {
          this.experimentService = experimentService;
      }

    @PostMapping
    public ResponseEntity<ExperimentResponse> createExperiment(
            @RequestBody Experiment experiment) {

        Experiment savedExperiment =
                experimentService.setActiveExperiment(experiment);

        ExperimentResponse response = new ExperimentResponse(
                savedExperiment.getId(),
                savedExperiment.getName(),
                savedExperiment.getCreatedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

       @GetMapping
    public Experiment getAllExperiments() {
          return experimentService.getActiveExperiment();
       }

    @GetMapping("/history")
    public List<Experiment> getExperimentHistory() {
        return experimentService.getExperimentHistory();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Experiment> getExperimentById(
            @PathVariable Integer id) {

        Experiment experiment = experimentService.getExperimentById(id);


        return ResponseEntity.ok(experiment);
    }






}
