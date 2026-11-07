package com.faultlens.demo.Metrics.Repository;

import com.faultlens.demo.Metrics.Model.ExperimentResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExperimentResultRepository extends JpaRepository<ExperimentResult, Integer> {

List<ExperimentResult> findByExperimentId(Integer experimentId);

    Optional<ExperimentResult> findByExperiment_Id(Integer experimentId);


}
