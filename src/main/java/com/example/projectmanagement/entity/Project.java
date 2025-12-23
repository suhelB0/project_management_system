package com.example.projectmanagement.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;

    @Column(nullable = false, unique = true)
    private String projectName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_type_id",  nullable = false)
    private ProjectType projectType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_subtype_id", nullable = false)
    private ProjectSubType projectSubType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime projectCreationTimestamp;

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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getProjectCreationTimestamp() {
        return projectCreationTimestamp;
    }

    public void setProjectCreationTimestamp(LocalDateTime projectCreationTimestamp) {
        this.projectCreationTimestamp = projectCreationTimestamp;
    }
}
