package com.example.projectmanagement.repository;

import com.example.projectmanagement.entity.Project;
import com.example.projectmanagement.enums.ProjectCriteria;

import java.util.List;

public interface ProjectRepositoryCustom {
    int deleteByCriteria(ProjectCriteria byWhich, String value);
    List<Project> findByCriteria(ProjectCriteria byWhich, String value);
}
