package com.example.projectmanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class ProjectSubTypeRequest {
    @NotBlank(message = "project sub-type is required")
    private String projectSubTypeName;

    public String getProjectSubTypeName() {
        return projectSubTypeName;
    }

    public void setProjectSubTypeName(String projectSubTypeName) {
        this.projectSubTypeName = projectSubTypeName;
    }
}
