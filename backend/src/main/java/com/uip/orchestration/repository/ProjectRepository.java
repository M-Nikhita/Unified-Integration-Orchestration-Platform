package com.uip.orchestration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uip.orchestration.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}