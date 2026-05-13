package com.uip.orchestration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.uip.orchestration.service.WorkflowExecutionService;

@RestController
@RequestMapping("/api/workflows")
@RequiredArgsConstructor
public class WorkflowExecutionController {

    private final WorkflowExecutionService executionService;

    @PostMapping("/{workflowId}/execute")
    public String executeWorkflow(@PathVariable Long workflowId) {
        return executionService.executeWorkflow(workflowId);
    }
}