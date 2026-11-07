package com.faultlens.demo.Experiment.Model;


import java.time.LocalDateTime;

public record ExperimentResponse(
        Integer id,
        String name,
        LocalDateTime createdAt
) {}
