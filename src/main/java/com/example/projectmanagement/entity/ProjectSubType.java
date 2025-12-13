package com.example.projectmanagement.entity;

import jakarta.persistence.*;

@Entity
public class ProjectSubType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectSubTypeId;

    @Column(nullable = false, unique = true)
    private String projectSubTypeName;

    public Integer getProjectSubTypeId() {
        return projectSubTypeId;
    }

    public void setProjectSubTypeId(Integer projectSubTypeId) {
        this.projectSubTypeId = projectSubTypeId;
    }

    public String getProjectSubTypeName() {
        return projectSubTypeName;
    }

    public void setProjectSubTypeName(String projectSubTypeName) {
        this.projectSubTypeName = projectSubTypeName;
    }
}
