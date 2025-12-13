package com.example.projectmanagement.controller;

import com.example.projectmanagement.dto.ProjectTypeRequest;
import com.example.projectmanagement.dto.ProjectTypeResponse;
import com.example.projectmanagement.service.ProjectTypeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projectTypes")
public class ProjectTypeController {

    private final ProjectTypeService projectTypeService;

    public ProjectTypeController(ProjectTypeService projectTypeService){
        this.projectTypeService = projectTypeService;
    }

    @PostMapping
    public ProjectTypeResponse createProjectType(@Valid @RequestBody ProjectTypeRequest request){
        return projectTypeService.createProjectType(request);
    }

    @GetMapping("/{id}")
    public ProjectTypeResponse getProjectTypeById(@PathVariable Integer id){
        return projectTypeService.getProjectTypeById(id);
    }

    @GetMapping
    public List<ProjectTypeResponse> getAllProjectType(){
        return projectTypeService.getAllProjectType();
    }
}
