package com.example.projectmanagement.controller;

import com.example.projectmanagement.dto.ProjectSubTypeRequest;
import com.example.projectmanagement.dto.ProjectSubTypeResponse;
import com.example.projectmanagement.entity.ProjectSubType;
import com.example.projectmanagement.service.ProjectSubTypeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projectSubType")
public class ProjectSubTypeController {

    private final ProjectSubTypeService projectSubTypeService;

    public ProjectSubTypeController(ProjectSubTypeService projectSubTypeService) {
        this.projectSubTypeService = projectSubTypeService;
    }

    @PostMapping
    public ProjectSubTypeResponse createProjectSubType(@Valid @RequestBody ProjectSubTypeRequest request) {
        return projectSubTypeService.createProjectSubType(request);
    }

    @GetMapping
    public List<ProjectSubTypeResponse> getAllProjectSubType() {
        return projectSubTypeService.getAllProjectSubType();
    }

    @GetMapping("/{id}")
    public ProjectSubTypeResponse getProjectSubTypeById(@PathVariable Integer id) {
        return projectSubTypeService.getProjectSubTypeById(id);
    }
}
