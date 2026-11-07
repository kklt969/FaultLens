package com.faultlens.demo.Experiment.Repository;


import com.faultlens.demo.Experiment.Model.Experiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExperimentRepository extends JpaRepository<Experiment, Integer> {

    List<Experiment> findAllByOrderByIdDesc();
    List<Experiment> findAllByOrderByIdAsc();
    @Query("SELECT e.durationSeconds FROM Experiment e WHERE e.id = :experimentId")
    Optional<Integer> findDurationByExperimentId(
            @Param("experimentId") Integer experimentId);
}
