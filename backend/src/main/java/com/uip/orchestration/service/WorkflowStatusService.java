package com.uip.orchestration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

import com.uip.orchestration.repository.WorkflowExecutionLogRepository;

@Service
@RequiredArgsConstructor
public class WorkflowStatusService {

    private final WorkflowExecutionLogRepository logRepository;

    public Map<String, Object> getWorkflowStatus(Long workflowId) {

        long total = logRepository.countByWorkflowId(workflowId);
        long success = logRepository.countByWorkflowIdAndStatus(workflowId, "SUCCESS");
        long failed = logRepository.countByWorkflowIdAndStatus(workflowId, "FAILED");

        String overallStatus = failed > 0 ? "FAILED" : "SUCCESS";

        Map<String, Object> status = new HashMap<>();
        status.put("workflowId", workflowId);
        status.put("totalSteps", total);
        status.put("successSteps", success);
        status.put("failedSteps", failed);
        status.put("overallStatus", overallStatus);

        return status;
    }
}