package com.uip.orchestration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.uip.orchestration.model.WorkflowStep;
import com.uip.orchestration.repository.WorkflowStepRepository;

@Service
@RequiredArgsConstructor
public class WorkflowStepService {

    private final WorkflowStepRepository workflowStepRepository;

    public WorkflowStep addStep(WorkflowStep step) {
        return workflowStepRepository.save(step);
    }
}