package com.example.projectmanagement.controller;

import com.example.projectmanagement.dto.ProjectRequest;
import com.example.projectmanagement.dto.ProjectResponse;
import com.example.projectmanagement.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ProjectResponse createProject(@Valid @RequestBody ProjectRequest request) {
        return projectService.createProject(request);
    }

    @GetMapping
    public List<ProjectResponse> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ProjectResponse getById(@PathVariable Integer id) {
        return projectService.getProjectById(id);
    }

    @GetMapping("/byType/{typeId}")
    public List<ProjectResponse> getByType(@PathVariable Integer typeId) {
        return projectService.getProjectsByType(typeId);
    }

    @GetMapping("/bySubType/{subTypeId}")
    public List<ProjectResponse> getBySubType(@PathVariable Integer subTypeId) {
        return projectService.getProjectsBySubType(subTypeId);
    }

    @PutMapping("/{id}")
    public ProjectResponse updateProject(@PathVariable Integer id, @Valid @RequestBody ProjectRequest request) {
        return projectService.updateProject(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        projectService.deleteProjectById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProject(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer typeId,
            @RequestParam(required = false) Integer subTypeId) {

        if (name != null) {
            projectService.deleteProjectByName(name);
            return ResponseEntity.noContent().build();
        }

        if (typeId != null) {
            projectService.deleteProjectByTypeId(typeId);
            return ResponseEntity.noContent().build();
        }

        if (subTypeId != null) {
            projectService.deleteProjectBySubTypeId(subTypeId);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().build();
    }

}
