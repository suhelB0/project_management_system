package com.example.projectmanagement.service;

import com.example.projectmanagement.dto.ProjectSubTypeRequest;
import com.example.projectmanagement.dto.ProjectSubTypeResponse;
import com.example.projectmanagement.entity.ProjectSubType;
import com.example.projectmanagement.exception.DuplicateResourceException;
import com.example.projectmanagement.exception.ResourceNotFoundException;
import com.example.projectmanagement.repository.ProjectSubTypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectSubTypeService {

    private final ProjectSubTypeRepository projectSubTypeRepository;

    public ProjectSubTypeService(ProjectSubTypeRepository projectSubTypeRepository) {
        this.projectSubTypeRepository = projectSubTypeRepository;
    }

    public ProjectSubTypeResponse createProjectSubType(ProjectSubTypeRequest request) {
        projectSubTypeRepository.findByProjectSubTypeName(request.getProjectSubTypeName()).ifPresent(p ->{
            throw new DuplicateResourceException("Project sub-type with name '" + request.getProjectSubTypeName() + "' already exists");
        });

        ProjectSubType projectSubType = new ProjectSubType();
        projectSubType.setProjectSubTypeName(request.getProjectSubTypeName());

        ProjectSubType saved = projectSubTypeRepository.save(projectSubType);
        return toResponse(saved);
    }

    public List<ProjectSubTypeResponse> getAllProjectSubType() {
        List<ProjectSubType> projectSubTypes = projectSubTypeRepository.findAll();
        List<ProjectSubTypeResponse> projectSubTypeResponses = new ArrayList<>();
        for (ProjectSubType projectSubType : projectSubTypes) {
            projectSubTypeResponses.add(toResponse(projectSubType));
        }

        return projectSubTypeResponses;
    }

    public ProjectSubTypeResponse getProjectSubTypeById(Integer id) {
        ProjectSubType projectSubType = projectSubTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project Sub-Type not found with id " + id));
        return toResponse(projectSubType);
    }

    private ProjectSubTypeResponse toResponse(ProjectSubType saved) {
        ProjectSubTypeResponse response = new ProjectSubTypeResponse();
        response.setProjectSubTypeName(saved.getProjectSubTypeName());
        response.setProjectSubTypeId(saved.getProjectSubTypeId());
        return response;
    }
}
