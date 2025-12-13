package com.example.projectmanagement.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;

    private String projectName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_type_id",  nullable = false)
    private ProjectType projectType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_subtype_id", nullable = false)
    private ProjectSubType projectSubType;

    @CreationTimestamp
    @Column(updatable = false)
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

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public ProjectSubType getProjectSubType() {
        return projectSubType;
    }

    public void setProjectSubType(ProjectSubType projectSubType) {
        this.projectSubType = projectSubType;
    }

    public Timestamp getProjectCreationTimestamp() {
        return projectCreationTimestamp;
    }

    public void setProjectCreationTimestamp(Timestamp projectCreationTimestamp) {
        this.projectCreationTimestamp = projectCreationTimestamp;
    }
}
