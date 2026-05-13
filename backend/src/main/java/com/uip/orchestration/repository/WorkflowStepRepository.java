package com.uip.orchestration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.uip.orchestration.model.WorkflowStep;

public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, Long> {

    List<WorkflowStep> findByWorkflowIdOrderByStepOrder(Long workflowId);

}
