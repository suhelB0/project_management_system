package com.example.projectmanagement.service;

import com.example.projectmanagement.dto.ProjectTypeRequest;
import com.example.projectmanagement.dto.ProjectTypeResponse;
import com.example.projectmanagement.entity.ProjectType;
import com.example.projectmanagement.exception.ResourceNotFoundException;
import com.example.projectmanagement.repository.ProjectTypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectTypeService {

    private final ProjectTypeRepository projectTypeRepository;

    public ProjectTypeService(ProjectTypeRepository projectTypeRepository) {
        this.projectTypeRepository = projectTypeRepository;
    }

    public ProjectTypeResponse createProjectType(ProjectTypeRequest request) {
        ProjectType projectType = new ProjectType();
        projectType.setProjectTypeName(request.getProjectTypeName());

        ProjectType saved = projectTypeRepository.save(projectType);
        return toResponse(saved);
    }

    public ProjectTypeResponse getProjectTypeById(Integer id) {
        ProjectType projectType = projectTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProjectType not found with id " + id));
        return toResponse(projectType);
    }

    public List<ProjectTypeResponse> getAllProjectType() {
        List<ProjectType> projectTypes = projectTypeRepository.findAll();
        List<ProjectTypeResponse> projectTypeResponses = new ArrayList<>();
        for(ProjectType projectType : projectTypes) {
            projectTypeResponses.add(toResponse(projectType));
        }
        return projectTypeResponses;
    }

    private ProjectTypeResponse toResponse(ProjectType saved) {
        ProjectTypeResponse response = new ProjectTypeResponse();
        response.setProjectTypeId(saved.getProjectTypeId());
        response.setProjectTypeName(saved.getProjectTypeName());
        return response;
    }
}
