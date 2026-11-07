package com.faultlens.demo.Report.Service;

import com.faultlens.demo.Report.Model.ResilienceAnalysisRequest;
import com.faultlens.demo.Report.Model.ResilienceReport;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Component
public class OllamaClient {





    private final RestClient restClient;
    private final ObjectMapper objectMapper; // convert java object to json and json back to java objects

    public OllamaClient(ObjectMapper objectMapper) {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:11434")
                .build();

        this.objectMapper = objectMapper;
    }

    public ResilienceReport generateReport(
            ResilienceAnalysisRequest analysisRequest) {

        try {
            String inputJson =
                    objectMapper.writeValueAsString(analysisRequest);

            Map<String, Object> request = Map.of(
                    "model", "llama3.2:3b",
                    "stream", false,
                    "format", "json",
                    "messages", List.of(
                            Map.of(
                                    "role", "system",
                                    "content", """
                You are a senior backend reliability engineer
                analyzing controlled resilience experiments.

                Your task is to interpret the provided experiment
                comparison and produce a useful technical report.

                RULES:

                1. DATA GROUNDING
                - Base observations strictly on provided data.
                - Never invent metrics or system behavior.
                - Java-calculated metrics are authoritative.
                - Do not calculate or modify numerical metrics.

                2. ANALYTICAL REASONING
                - Explain what changed between baseline and
                  fault-injected experiments.
                - Explain why those changes matter for reliability.
                - Distinguish observed facts from possible explanations.
                - Do not simply repeat input metrics.

                3. HYPOTHESES
                - Provide testable explanations for observations.
                - Clearly identify hypotheses as possibilities.
                - Avoid generic statements unrelated to the experiment.

                4. RECOMMENDATIONS
                - Provide actionable engineering recommendations.
                - Address weaknesses observed in the experiment.
                - Never contradict the supplied evidence.

                5. LIMITATIONS
                - Explain what the experiment cannot establish.
                - Do not claim causation without sufficient evidence.

                6. WRITING QUALITY
                - Be technically precise and concise.
                - Avoid repetitive observations.
                - Write for backend engineers.

                Return valid JSON with these fields:
                summary, observations, hypotheses,
                recommendations, limitations.

                Summary must be a string.
                All other fields must be arrays of strings.
                Return JSON only, without Markdown.
                
                                            STRICT EVIDENCE RULES:
                                            
                                            - Never assume retries occurred unless retry data is provided.
                                            - Never attribute failures to Redis, CPU, memory, or other
                                              components unless the input contains supporting evidence.
                                            - Do not present hypotheses as confirmed causal explanations.
                                            - Every observation must be traceable to supplied experiment data.
                                            - Recommendations must be connected to an observed weakness.
                                            - If evidence is insufficient, explicitly state that.
                                            
                                            FAULT TYPE CONSISTENCY RULES:
                                            
                                                                        1. Always identify the fault type using the supplied experiment metadata.
                                                                        2. Never describe a DROP experiment as LATENCY.
                                                                        3. Never describe a LATENCY experiment as DROP.
                                                                        4. Recommendations must correspond to the actual injected fault.
                                                                        5. Do not assume Redis, databases, or other dependencies are involved
                                                                           unless explicitly supported by the supplied data.
                                                                        6. If experiment metadata conflicts with metric patterns,
                                                                           report the inconsistency instead of guessing.
                                                                        7. Distinguish measured observations from possible explanations.
                                            
                                            SUMMARY:
                                            - Write a meaningful 2-4 sentence technical summary.
                                            - Explain the main difference between the experiments.
                                            - Explain the reliability implications.
                                            - Do not return a report title as the summary.
                
                """
                            ),
                            Map.of(
                                    "role", "user",
                                    "content", inputJson
                            )
                    )
            );

            JsonNode response = restClient.post()
                    .uri("/api/chat")
                    .body(request)
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null
                    || !response.hasNonNull("message")
                    || !response.path("message")
                    .hasNonNull("content")) {
                throw new IllegalStateException(
                        "Ollama returned an empty response."
                );
            }

            String reportJson =
                    response.path("message").path("content").asText();

            return objectMapper.readValue(
                    reportJson, ResilienceReport.class
            );

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to generate resilience report with Ollama.",
                    e
            );
        }
    }
}
