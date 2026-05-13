package com.uip.orchestration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.uip.orchestration.model.Connector;
import com.uip.orchestration.service.ConnectorService;

@RestController
@RequestMapping("/api/connectors")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ConnectorController {

    private final ConnectorService connectorService;

    @PostMapping
    public Connector createConnector(@RequestBody Connector connector) {
        return connectorService.createConnector(connector);
    }

    @GetMapping("/workflow/{workflowId}")
    public List<Connector> getConnectorsByWorkflow(@PathVariable Long workflowId) {
        return connectorService.getConnectorsByWorkflow(workflowId);
    }
}