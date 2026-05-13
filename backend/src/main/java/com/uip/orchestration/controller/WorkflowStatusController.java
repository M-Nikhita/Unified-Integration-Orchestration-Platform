package com.uip.orchestration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import com.uip.orchestration.service.WorkflowStatusService;

@RestController
@RequestMapping("/api/workflows")
@RequiredArgsConstructor
public class WorkflowStatusController {

    private final WorkflowStatusService workflowStatusService;

    @GetMapping("/{id}/status")
    public Map<String, Object> getWorkflowStatus(@PathVariable Long id) {
        return workflowStatusService.getWorkflowStatus(id);
    }
}