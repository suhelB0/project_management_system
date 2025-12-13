package com.example.projectmanagement.repository;

import com.example.projectmanagement.entity.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectTypeRepository extends JpaRepository<ProjectType, Integer> {
    Optional<ProjectType> findByProjectTypeName(String projectTypeName);
}
