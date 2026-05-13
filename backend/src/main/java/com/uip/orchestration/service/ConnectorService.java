package com.uip.orchestration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import com.uip.orchestration.model.Connector;
import com.uip.orchestration.repository.ConnectorRepository;

@Service
@RequiredArgsConstructor
public class ConnectorService {

    private final ConnectorRepository connectorRepository;

    public Connector createConnector(Connector connector) {
        return connectorRepository.save(connector);
    }

    public List<Connector> getConnectorsByWorkflow(Long workflowId) {
        return connectorRepository.findByWorkflowId(workflowId);
    }
}