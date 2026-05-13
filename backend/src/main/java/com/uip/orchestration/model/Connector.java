package com.uip.orchestration.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Connector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;  // REST, DB, etc.

    @Column(columnDefinition = "TEXT")
    private String configuration;  // JSON configuration

    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;
}