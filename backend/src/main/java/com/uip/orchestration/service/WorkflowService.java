package com.uip.orchestration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import com.uip.orchestration.model.Workflow;
import com.uip.orchestration.repository.WorkflowRepository;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowRepository workflowRepository;

    public Workflow createWorkflow(Workflow workflow) {
        return workflowRepository.save(workflow);
    }

    public List<Workflow> getWorkflowsByProject(Long projectId) {
        return workflowRepository.findByProjectId(projectId);
    }
    public List<Workflow> getAllWorkflows() {
        return workflowRepository.findAll();
    }
}