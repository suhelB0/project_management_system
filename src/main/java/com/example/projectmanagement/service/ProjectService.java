package com.example.projectmanagement.service;

import com.example.projectmanagement.dto.*;
import com.example.projectmanagement.entity.Project;
import com.example.projectmanagement.entity.ProjectSubType;
import com.example.projectmanagement.entity.ProjectType;
import com.example.projectmanagement.enums.ProjectCriteria;
import com.example.projectmanagement.exception.ResourceNotFoundException;
import com.example.projectmanagement.repository.ProjectRepository;
import com.example.projectmanagement.repository.ProjectSubTypeRepository;
import com.example.projectmanagement.repository.ProjectTypeRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectTypeRepository projectTypeRepository;
    private final ProjectSubTypeRepository projectSubTypeRepository;

    public ProjectService(ProjectRepository projectRepository, ProjectTypeRepository projectTypeRepository, ProjectSubTypeRepository projectSubTypeRepository) {
        this.projectRepository = projectRepository;
        this.projectTypeRepository = projectTypeRepository;
        this.projectSubTypeRepository = projectSubTypeRepository;
    }

    public ProjectResponse createProject(ProjectRequest request) {
        ProjectType type = projectTypeRepository.findById(request.getProjectTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("ProjectType not found with id " + request.getProjectTypeId()));

        ProjectSubType subType = projectSubTypeRepository.findById(request.getProjectSubTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("ProjectSubType not found with id " + request.getProjectSubTypeId()));

        String username = getCurrentUsername();

        Project project = new Project();
        project.setProjectName(request.getProjectName());
        project.setProjectType(type);
        project.setProjectSubType(subType);
        project.setCreatedBy(username);

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

        String username = getCurrentUsername();

        boolean isAdmin = hasPermission("ADMIN");
        boolean isManager = hasPermission("MANAGER");

        if(!isAdmin){
            if(isManager){
                if(!existing.getCreatedBy().equals(username)){
                    throw new AccessDeniedException("You are not allowed to update projects");
                }
            }
        }

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
        response.setCreatedBy(project.getCreatedBy());

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

    private String getCurrentUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        System.out.println(jwt.toString());
        return jwt.getClaimAsString("preferred_username");
    }

    private boolean hasPermission(String permission){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + permission));
    }
}
