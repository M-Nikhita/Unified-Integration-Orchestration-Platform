package com.uip.orchestration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.uip.orchestration.model.Workflow;
import com.uip.orchestration.service.WorkflowService;

@RestController
@RequestMapping("/api/workflows")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;

    @PostMapping
    public Workflow createWorkflow(@RequestBody Workflow workflow) {
        return workflowService.createWorkflow(workflow);
    }

    @GetMapping("/project/{projectId}")
    public List<Workflow> getWorkflowsByProject(@PathVariable Long projectId) {
        return workflowService.getWorkflowsByProject(projectId);
    }
    @GetMapping
    public List<Workflow> getAllWorkflows() {
        return workflowService.getAllWorkflows();
    }
}
