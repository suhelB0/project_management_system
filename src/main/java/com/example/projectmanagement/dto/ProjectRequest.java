package com.example.projectmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProjectRequest {
    @NotBlank(message = "projectName is required")
    private String projectName;

    @NotNull(message = "projectTypeId is required")
    private Integer projectTypeId;

    @NotNull(message = "projectSubTypeId is required")
    private Integer projectSubTypeId;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getProjectTypeId() {
        return projectTypeId;
    }

    public void setProjectTypeId(Integer projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    public Integer getProjectSubTypeId() {
        return projectSubTypeId;
    }

    public void setProjectSubTypeId(Integer projectSubTypeId) {
        this.projectSubTypeId = projectSubTypeId;
    }
}
