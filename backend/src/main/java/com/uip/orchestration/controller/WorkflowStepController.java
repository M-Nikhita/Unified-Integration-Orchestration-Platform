package com.uip.orchestration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.uip.orchestration.model.WorkflowStep;
import com.uip.orchestration.service.WorkflowStepService;

@RestController
@RequestMapping("/api/workflow-steps")
@RequiredArgsConstructor
public class WorkflowStepController {

    private final WorkflowStepService workflowStepService;

    @PostMapping
    public WorkflowStep addStep(@RequestBody WorkflowStep step) {
        return workflowStepService.addStep(step);
    }
}