package com.example.projectmanagement.service;

import com.example.projectmanagement.dto.ProjectRequest;
import com.example.projectmanagement.dto.ProjectResponse;
import com.example.projectmanagement.dto.ProjectSubTypeResponse;
import com.example.projectmanagement.dto.ProjectTypeResponse;
import com.example.projectmanagement.entity.Project;
import com.example.projectmanagement.entity.ProjectSubType;
import com.example.projectmanagement.entity.ProjectType;
import com.example.projectmanagement.enums.ProjectCriteria;
import com.example.projectmanagement.exception.ResourceNotFoundException;
import com.example.projectmanagement.repository.ProjectRepository;
import com.example.projectmanagement.repository.ProjectSubTypeRepository;
import com.example.projectmanagement.repository.ProjectTypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectTypeRepository projectTypeRepository;
    private final ProjectSubTypeRepository projectSubTypeRepository;

    public ProjectService(ProjectRepository projectRepository, ProjectTypeRepository projectTypeRepository,ProjectSubTypeRepository projectSubTypeRepository) {
        this.projectRepository = projectRepository;
        this.projectTypeRepository = projectTypeRepository;
        this.projectSubTypeRepository = projectSubTypeRepository;
    }

    public ProjectResponse createProject(ProjectRequest request) {
        ProjectType type = projectTypeRepository.findById(request.getProjectTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("ProjectType not found with id " + request.getProjectTypeId()));

        ProjectSubType subType = projectSubTypeRepository.findById(request.getProjectSubTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("ProjectSubType not found with id " + request.getProjectSubTypeId()));

        Project project = new Project();
        project.setProjectName(request.getProjectName());
        project.setProjectType(type);
        project.setProjectSubType(subType);

        Project saved = projectRepository.save(project);
        return toResponse(saved);
    }

    public List<ProjectResponse> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectResponse> projectResponses = new ArrayList<>();

        for (Project project : projects) {
            projectResponses.add(toResponse(project));
        }
        return projectResponses;
    }

    public List<ProjectResponse> getProjectsByCriteria(ProjectCriteria criteria, String value) {
        List<Project> projects = projectRepository.findByCriteria(criteria, value);

        if (projects.isEmpty()) {
            throw new ResourceNotFoundException("Project not found");
        }

        List<ProjectResponse> projectResponses = new ArrayList<>();
        for (Project project : projects) {
            projectResponses.add(toResponse(project));
        }
        return projectResponses;
    }

    public int deleteProjectByCriteria(ProjectCriteria criteria, String value) {
        return projectRepository.deleteByCriteria(criteria, value);
    }

    public ProjectResponse updateProject(Integer projectId, ProjectRequest request) {
        Project existing = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));

        existing.setProjectName(request.getProjectName());

        ProjectType type = projectTypeRepository.findById(request.getProjectTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("ProjectType not found with id " + request.getProjectTypeId()));
        ProjectSubType subType = projectSubTypeRepository.findById(request.getProjectSubTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("ProjectSubType not found with id " + request.getProjectSubTypeId()));

        existing.setProjectType(type);
        existing.setProjectSubType(subType);

        Project updated = projectRepository.save(existing);
        return toResponse(updated);
    }

    private ProjectResponse toResponse(Project project) {
        ProjectResponse response = new ProjectResponse();

        response.setProjectId(project.getProjectId());
        response.setProjectName(project.getProjectName());
        response.setProjectCreationTimestamp(project.getProjectCreationTimestamp());

        ProjectTypeResponse typeRes = new ProjectTypeResponse();
        typeRes.setProjectTypeId(project.getProjectType().getProjectTypeId());
        typeRes.setProjectTypeName(project.getProjectType().getProjectTypeName());
        response.setProjectType(typeRes);

        ProjectSubTypeResponse subTypeRes = new ProjectSubTypeResponse();
        subTypeRes.setProjectSubTypeId(project.getProjectSubType().getProjectSubTypeId());
        subTypeRes.setProjectSubTypeName(project.getProjectSubType().getProjectSubTypeName());
        response.setProjectSubType(subTypeRes);

        return response;
    }
}
