package com.example.projectmanagement.service;

import com.example.projectmanagement.dto.ProjectTypeRequest;
import com.example.projectmanagement.dto.ProjectTypeResponse;
import com.example.projectmanagement.entity.ProjectType;
import com.example.projectmanagement.exception.DuplicateResourceException;
import com.example.projectmanagement.exception.ResourceNotFoundException;
import com.example.projectmanagement.repository.ProjectTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectTypeService {

    private final ProjectTypeRepository projectTypeRepository;

    public ProjectTypeService(ProjectTypeRepository projectTypeRepository) {
        this.projectTypeRepository = projectTypeRepository;
    }

    public ProjectTypeResponse createProjectType(ProjectTypeRequest request) {
        projectTypeRepository.findByProjectTypeName(request.getProjectTypeName()).ifPresent(p -> {
            throw new DuplicateResourceException("Project type with name '" + request.getProjectTypeName() + "' already exists");
        });

        ProjectType projectType = new ProjectType();
        projectType.setProjectTypeName(request.getProjectTypeName());

        ProjectType saved = projectTypeRepository.save(projectType);
        return toResponse(saved);
    }

    public ProjectTypeResponse getProjectTypeById(Integer id) {
        return projectTypeRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("ProjectType not found with id " + id));
    }

    public List<ProjectTypeResponse> getAllProjectType() {
        return projectTypeRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    private ProjectTypeResponse toResponse(ProjectType saved) {
        ProjectTypeResponse response = new ProjectTypeResponse();
        response.setProjectTypeId(saved.getProjectTypeId());
        response.setProjectTypeName(saved.getProjectTypeName());
        return response;
    }
}
