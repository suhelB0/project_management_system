package com.example.projectmanagement.service;

import com.example.projectmanagement.dto.ProjectRequest;
import com.example.projectmanagement.dto.ProjectResponse;
import com.example.projectmanagement.dto.ProjectSubTypeResponse;
import com.example.projectmanagement.dto.ProjectTypeResponse;
import com.example.projectmanagement.entity.Project;
import com.example.projectmanagement.entity.ProjectSubType;
import com.example.projectmanagement.entity.ProjectType;
import com.example.projectmanagement.exception.DuplicateResourceException;
import com.example.projectmanagement.exception.ResourceNotFoundException;
import com.example.projectmanagement.repository.ProjectRepository;
import com.example.projectmanagement.repository.ProjectSubTypeRepository;
import com.example.projectmanagement.repository.ProjectTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        projectRepository.findByProjectName(request.getProjectName()).ifPresent(p -> {
            throw new DuplicateResourceException("Project with name '" + request.getProjectName() + "' already exists");
        });

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
        return projectRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ProjectResponse getProjectById(Integer id) {
        return projectRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + id));
    }

    public List<ProjectResponse> getProjectsByType(Integer projectTypeId) {
        ProjectType type = projectTypeRepository.findById(projectTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("ProjectType not found with id " + projectTypeId));
        return projectRepository.findAllByProjectType(type).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ProjectResponse> getProjectsBySubType(Integer projectSubTypeId) {
        ProjectSubType subType = projectSubTypeRepository.findById(projectSubTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("ProjectSubType not found with id " + projectSubTypeId));
        return projectRepository.findAllByProjectSubType(subType).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public void deleteProjectById(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id " + projectId);
        }
        projectRepository.deleteById(projectId);
    }

    public void deleteProjectByName(String projectName) {
        projectRepository.findByProjectName(projectName)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with name " + projectName));
        projectRepository.deleteByProjectName(projectName);
    }

    public void deleteProjectByTypeId(Integer typeId) {
        List<Project> projects = projectRepository.findByProjectType_ProjectTypeId(typeId);
        if (projects.isEmpty()) {
            throw new ResourceNotFoundException("No projects found with projectTypeId: " + typeId);
        }
        projectRepository.deleteAll(projects);
    }

    public void deleteProjectBySubTypeId(Integer subTypeId) {
        List<Project> projects = projectRepository.findByProjectSubType_ProjectSubTypeId(subTypeId);
        if (projects.isEmpty()) {
            throw new ResourceNotFoundException("No projects found with projectSubTypeId: " + subTypeId);
        }
        projectRepository.deleteAll(projects);
    }

    public ProjectResponse updateProject(Integer projectId, ProjectRequest request) {
        Project existing = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));

        if (!existing.getProjectName().equalsIgnoreCase(request.getProjectName())) {
            projectRepository.findByProjectName(request.getProjectName()).ifPresent(p -> {
                throw new DuplicateResourceException("Project with name '" + request.getProjectName() + "' already exists");
            });
            existing.setProjectName(request.getProjectName());
        }

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
