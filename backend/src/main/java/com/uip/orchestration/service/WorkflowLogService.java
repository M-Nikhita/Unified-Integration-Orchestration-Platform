package com.uip.orchestration.service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.uip.orchestration.model.WorkflowStep;
import com.uip.orchestration.model.Connector;
import com.uip.orchestration.model.WorkflowExecutionLog;

import com.uip.orchestration.repository.WorkflowStepRepository;
import com.uip.orchestration.repository.WorkflowExecutionLogRepository;
@Service
@RequiredArgsConstructor
public class WorkflowLogService {

    private final WorkflowExecutionLogRepository logRepository;

    public List<WorkflowExecutionLog> getLogs(Long workflowId) {

        return logRepository.findByWorkflowId(workflowId);

    }
}