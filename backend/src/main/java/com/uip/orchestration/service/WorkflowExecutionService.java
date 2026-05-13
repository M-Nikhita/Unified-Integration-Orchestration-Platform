package com.uip.orchestration.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.uip.orchestration.model.WorkflowExecutionLog;
import com.uip.orchestration.model.WorkflowStep;
import com.uip.orchestration.model.Connector;

import com.uip.orchestration.repository.WorkflowStepRepository;
import com.uip.orchestration.repository.WorkflowExecutionLogRepository;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
@Service
@RequiredArgsConstructor
public class WorkflowExecutionService {

    private final WorkflowExecutionLogRepository logRepository;

    private final WorkflowStepRepository workflowStepRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public String executeWorkflow(Long workflowId) {

        try {

            List<WorkflowStep> steps =
                    workflowStepRepository.findByWorkflowIdOrderByStepOrder(workflowId);

            ObjectMapper mapper = new ObjectMapper();

            ExecutorService executor = Executors.newFixedThreadPool(5);

            List<Future<?>> futures = new ArrayList<>();

            for (WorkflowStep step : steps) {

                Future<?> future = executor.submit(() -> {
                    executeStep(workflowId, step);
                });

                futures.add(future);
            }

            for (Future<?> future : futures) {
                future.get();
            }

            executor.shutdown();
            return "Workflow executed successfully";

        } catch (Exception e) {

            e.printStackTrace();
            return "Workflow execution failed";
        }
    }
    private void executeStep(Long workflowId, WorkflowStep step) {

        try {

            Connector connector = step.getConnector();

            ObjectMapper mapper = new ObjectMapper();

            String config = connector.getConfiguration();

            var jsonNode = mapper.readTree(config);

            String url = jsonNode.get("url").asText();
            String method = jsonNode.get("method").asText();

            String response = "";
            String status = "FAILED";

            int retryCount = 0;
            int maxRetries = 3;

            long startTime = System.currentTimeMillis();

            while (retryCount < maxRetries) {

                try {

                    if (method.equalsIgnoreCase("GET")) {

                        response = restTemplate.getForObject(url, String.class);

                    } else if (method.equalsIgnoreCase("POST")) {

                        response = restTemplate.postForObject(url, null, String.class);
                    }

                    status = "SUCCESS";
                    break;

                } catch (Exception ex) {

                    retryCount++;

                    System.out.println("Retry attempt: " + retryCount);

                    response = ex.getMessage();

                    if (retryCount == maxRetries) {
                        status = "FAILED";
                    }
                }
            }

            long executionTime = System.currentTimeMillis() - startTime;

            WorkflowExecutionLog log = new WorkflowExecutionLog();

            log.setWorkflowId(workflowId);
            log.setStepOrder(step.getStepOrder());
            log.setConnectorName(connector.getName());
            log.setRequestUrl(url);
            log.setResponse(response.substring(0, Math.min(response.length(), 2000)));
            log.setStatus(status);
            log.setExecutedAt(LocalDateTime.now());
            log.setExecutionTimeMs(executionTime);
            log.setRetryCount(retryCount);

            logRepository.save(log);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}