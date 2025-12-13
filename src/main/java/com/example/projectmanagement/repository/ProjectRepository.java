package com.example.projectmanagement.repository;

import com.example.projectmanagement.entity.Project;
import com.example.projectmanagement.entity.ProjectSubType;
import com.example.projectmanagement.entity.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Optional<Project> findByProjectName(String projectName);

    List<Project> findAllByProjectType(ProjectType type);

    List<Project> findAllByProjectSubType(ProjectSubType subType);

    void deleteByProjectName(String projectName);

    List<Project> findByProjectType_ProjectTypeId(Integer typeId);

    List<Project> findByProjectSubType_ProjectSubTypeId(Integer subTypeId);
}
