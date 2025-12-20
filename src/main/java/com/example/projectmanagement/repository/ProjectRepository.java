package com.example.projectmanagement.repository;

import com.example.projectmanagement.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer>, ProjectRepositoryCustom {
    Optional<Project> findByProjectName(String projectName);

}
