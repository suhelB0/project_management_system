package com.example.projectmanagement.controller;

import com.example.projectmanagement.dto.ProjectRequest;
import com.example.projectmanagement.dto.ProjectResponse;
import com.example.projectmanagement.enums.ProjectCriteria;
import com.example.projectmanagement.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
    public List<ProjectResponse> findAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/search")
    @Operation(
            summary = "Get projects by dynamic criteria",
            description = "Provide criteria type (byWhich) and its value to filter projects"
    )
    public ResponseEntity<List<ProjectResponse>> getProjectsByCriteria(
            @Parameter(
                    description = "Select search criteria",
                    required = true,
                    schema = @Schema(implementation = ProjectCriteria.class)
            )
            @RequestParam ProjectCriteria byWhich,

            @Parameter(
                    description = "Value for selected criteria",
                    required = true
            )
            @RequestParam String value) {

        List<ProjectResponse> projects = projectService.getProjectsByCriteria(byWhich, value);
        return ResponseEntity.ok(projects);
    }


    @PutMapping("/{id}")
    public ProjectResponse updateProject(@PathVariable Integer id, @Valid @RequestBody ProjectRequest request) {
        return projectService.updateProject(id, request);
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Delete project",
            description = "Select deletion criteria from dropdown and provide corresponding value"
    )
    public ResponseEntity<String> deleteProject(
            @Parameter(
                    description = "Select deletion criteria",
                    required = true,
                    schema = @Schema(
                            implementation = ProjectCriteria.class,
                            allowableValues = {"ID", "NAME", "TYPE_ID", "SUBTYPE_ID"}
                    )
            )
            @RequestParam ProjectCriteria criteria,

            @Parameter(
                    description = "Value for selected criteria",
                    required = true
            )
            @RequestParam String value) {

        int deletedCount = projectService.deleteProjectByCriteria(criteria, value);
        return ResponseEntity.ok("Deleted " + deletedCount + " project(s).");
    }

}
