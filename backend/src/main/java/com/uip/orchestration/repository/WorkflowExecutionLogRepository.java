package com.uip.orchestration.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.uip.orchestration.model.WorkflowExecutionLog;

public interface WorkflowExecutionLogRepository
        extends JpaRepository<WorkflowExecutionLog, Long> {

    List<WorkflowExecutionLog> findByWorkflowId(Long workflowId);

    long countByWorkflowId(Long workflowId);

    long countByWorkflowIdAndStatus(Long workflowId, String status);
}