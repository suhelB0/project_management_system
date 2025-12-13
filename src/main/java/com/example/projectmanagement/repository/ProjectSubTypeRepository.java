package com.example.projectmanagement.repository;

import com.example.projectmanagement.entity.ProjectSubType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectSubTypeRepository extends JpaRepository<ProjectSubType, Integer> {
    Optional<ProjectSubType> findByProjectSubTypeName(String projectSubTypeName);
}
