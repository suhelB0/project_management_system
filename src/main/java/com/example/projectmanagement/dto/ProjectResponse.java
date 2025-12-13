package com.example.projectmanagement.dto;

import java.sql.Timestamp;

public class ProjectResponse {
    private Integer projectId;
    private String projectName;
    private ProjectTypeResponse projectType;
    private ProjectSubTypeResponse projectSubType;
    private Timestamp projectCreationTimestamp;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public ProjectTypeResponse getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectTypeResponse projectType) {
        this.projectType = projectType;
    }

    public ProjectSubTypeResponse getProjectSubType() {
        return projectSubType;
    }

    public void setProjectSubType(ProjectSubTypeResponse projectSubType) {
        this.projectSubType = projectSubType;
    }

    public Timestamp getProjectCreationTimestamp() {
        return projectCreationTimestamp;
    }

    public void setProjectCreationTimestamp(Timestamp projectCreationTimestamp) {
        this.projectCreationTimestamp = projectCreationTimestamp;
    }
}
