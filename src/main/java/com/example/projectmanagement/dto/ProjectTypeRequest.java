package com.example.projectmanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class ProjectTypeRequest {
    @NotBlank(message = "project type is required")
    private String projectTypeName;

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }
}
