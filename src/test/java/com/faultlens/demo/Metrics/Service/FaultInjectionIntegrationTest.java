package com.faultlens.demo.Metrics.Service;

import com.faultlens.demo.Experiment.Model.Experiment;
import com.faultlens.demo.Experiment.Model.FaultType;
import com.faultlens.demo.Experiment.Service.ExperimentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)

class FaultInjectionIntegrationTest {
    @LocalServerPort // gives us the assigned port if we need to construct a url manuallky
    private int port; // start faultlens on an available port

    @Autowired
    private WebTestClient webTestClient; // sends HTTP requests to the running gateway


   @Autowired
    private ExperimentService experimentService;


    //@Test
    void errorFaultReturnsConfiguredStatus() {
        Experiment experiment = new Experiment();
        experiment.setName("Error integration test");
        experiment.setFaultType(FaultType.ERROR);
        experiment.setPercentage(100);
        experiment.setErrorCode(503);
        experiment.setDurationSeconds(30);
        experiment.setActive(true);

        experimentService.setActiveExperiment(experiment);

        webTestClient.get()
                .uri("http://localhost:" + port + "/api/cache/user2")
                .header("XAPI", "user2")
                .exchange()
                .expectStatus().isEqualTo(503);
    }

    @Test
    void dropFaultReturnsConfiguredStatus() {

        Experiment experiment = new Experiment();
        experiment.setName("Drop integration test");
        experiment.setFaultType(FaultType.DROP);
        experiment.setPercentage(100);
        experiment.setErrorCode(503);
        experiment.setDurationSeconds(30);
        experiment.setActive(true);

        experimentService.setActiveExperiment(experiment);

        webTestClient.get()
                .uri("http://localhost:" + port + "/api/cache/user2")
                .header("XAPI", "user2")
                .exchange()
                .expectStatus().isEqualTo(200);

    }


}
