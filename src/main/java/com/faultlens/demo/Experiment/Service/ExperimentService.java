package com.faultlens.demo.Experiment.Service;


import com.faultlens.demo.Experiment.Model.Experiment;
import com.faultlens.demo.Experiment.Repository.ExperimentRepository;
import com.faultlens.demo.Metrics.Model.ExperimentMetrics;
import com.faultlens.demo.Metrics.Model.ExperimentResult;
import com.faultlens.demo.Metrics.Repository.ExperimentResultRepository;
import com.faultlens.demo.Metrics.Service.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ExperimentService {

    private final AtomicReference<Experiment> activeExperiment = new AtomicReference<>();

    private final ExperimentRepository experimentRepository;

    private final ExperimentResultRepository experimentResultRepository;

    private final MetricsService metricsService;

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    public ExperimentService(ExperimentRepository experimentRepository, ExperimentResultRepository experimentResultRepository, MetricsService metricsService) {
        this.experimentRepository = experimentRepository;
        this.experimentResultRepository = experimentResultRepository;
        this.metricsService = metricsService;
    }

    public Experiment setActiveExperiment(Experiment experiment) {

        if(experiment.isActive()){

            ExperimentMetrics metrics = new ExperimentMetrics();
            experiment.setMetrics(metrics);

            Experiment savedExperiment = experimentRepository.save(experiment);

            activeExperiment.set(savedExperiment);


            scheduler.schedule(
                    ()->{
                            if (savedExperiment == activeExperiment.get() && savedExperiment.isActive()) {

                                savedExperiment.setActive(false);
                                activeExperiment.set(null);

                                experimentRepository.save(savedExperiment);

                                ExperimentResult result = metricsService.calculateResult(savedExperiment);

                                result.setExperiment(savedExperiment);
                                savedExperiment.setResult(result);

                                ExperimentResult savedResult =
                                        experimentResultRepository.saveAndFlush(result);


                            }

                        },
                    savedExperiment.getDurationSeconds(),
                        TimeUnit.SECONDS

            );
            return savedExperiment;

        }else{
            activeExperiment.set(null);
        }
        return null;

    }

    public List<Experiment> getExperimentHistory(){
        return experimentRepository.findAllByOrderByIdAsc();
    }

    public Experiment getExperimentById(Integer id){
        return experimentRepository.findById(id).orElse(null);


    }


    public Experiment getActiveExperiment() {


        return activeExperiment.get();
    }
}
