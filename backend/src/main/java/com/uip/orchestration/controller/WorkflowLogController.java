package com.uip.orchestration.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.uip.orchestration.model.WorkflowExecutionLog;
import com.uip.orchestration.service.WorkflowLogService;

@RestController
@RequestMapping("/api/workflows")
@RequiredArgsConstructor
public class WorkflowLogController {

    private final WorkflowLogService workflowLogService;

    @GetMapping("/{workflowId}/logs")
    public List<WorkflowExecutionLog> getWorkflowLogs(@PathVariable Long workflowId) {

        return workflowLogService.getLogs(workflowId);

    }
}