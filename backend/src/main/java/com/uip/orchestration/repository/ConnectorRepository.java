package com.uip.orchestration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.uip.orchestration.model.Connector;

public interface ConnectorRepository extends JpaRepository<Connector, Long> {

    List<Connector> findByWorkflowId(Long workflowId);
}