package com.faultlens.demo.filters;


import com.faultlens.demo.Experiment.Model.Experiment;
import com.faultlens.demo.Experiment.Model.FaultType;
import com.faultlens.demo.Experiment.Service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class LatencyFilter implements GlobalFilter {

    private final ExperimentService experimentService;

    @Autowired
    public LatencyFilter(ExperimentService experimentService) {
        this.experimentService = experimentService;
    }



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {



        long start = System.nanoTime();

        Experiment experiment = experimentService.getActiveExperiment();

        if (experiment == null) {
            return chain.filter(exchange);
        }

        experiment.getMetrics().incrementTotalRequests();

        FaultType faultType = experiment.getFaultType();

        int rand = ThreadLocalRandom.current().nextInt(100);

        Mono<Void> result;

        if (faultType == FaultType.LATENCY) {

            if (rand < experiment.getPercentage()) {
                result = Mono.delay(Duration.ofMillis(experiment.getLatencyMs()))
                        .then(chain.filter(exchange));
            } else {
                result = chain.filter(exchange);
            }

        } else if (faultType == FaultType.ERROR) {

            if (rand < experiment.getPercentage()) {
                exchange.getResponse().setStatusCode(
                        HttpStatusCode.valueOf(experiment.getErrorCode())
                );

                result = exchange.getResponse().setComplete();
            } else {
                result = chain.filter(exchange);
            }

        } else if (faultType == FaultType.DROP) {

            if (rand < experiment.getPercentage()) {
                experiment.getMetrics().incrementFailedRequests();
                result = exchange.getResponse().setComplete();
            } else {
                result = chain.filter(exchange);
            }

        } else {
            result = chain.filter(exchange);
        }

        return result.doFinally(signal -> {

            long duration = System.nanoTime() - start;

            HttpStatusCode status = exchange.getResponse().getStatusCode();

            int statusCode = status.value();

            if(statusCode >= 200 && statusCode < 400) {
                experiment.getMetrics().incrementSuccessfulRequests();
            }
            else{
                experiment.getMetrics().incrementFailedRequests();
            }

            experiment.getMetrics()
                    .addLatency(duration / 1_000_000);
        });
    }
}