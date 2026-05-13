package com.uip.orchestration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.uip.orchestration.model.Workflow;

public interface WorkflowRepository extends JpaRepository<Workflow, Long> {

    List<Workflow> findByProjectId(Long projectId);
}