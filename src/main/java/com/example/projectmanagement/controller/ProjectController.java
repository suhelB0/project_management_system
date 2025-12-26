package com.example.projectmanagement.controller;

import com.example.projectmanagement.dto.ProjectRequest;
import com.example.projectmanagement.dto.ProjectResponse;
import com.example.projectmanagement.enums.ProjectCriteria;
import com.example.projectmanagement.exception.ResourceNotFoundException;
import com.example.projectmanagement.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectRequest request) {
        try {
            ProjectResponse projectResponse = projectService.createProject(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(projectResponse);
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Project already exists");
        }
        catch (CannotCreateTransactionException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database is currently unavailable");
        }
        catch (DataAccessResourceFailureException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connection failed");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @GetMapping
    public ResponseEntity<?> findAllProjects() {
        try{
            return ResponseEntity.ok(projectService.getAllProjects());
        }
        catch (CannotCreateTransactionException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database is currently unavailable");
        }
        catch (DataAccessResourceFailureException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connection failed");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @GetMapping("/search")
    @Operation(
            summary = "Get projects by dynamic criteria",
            description = "Provide criteria type (byWhich) and its value to filter projects"
    )
    public ResponseEntity<?> getProjectsByCriteria(
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
        try{
            List<ProjectResponse> projects = projectService.getProjectsByCriteria(byWhich, value);
            return ResponseEntity.ok(projects);
        }
        catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        catch (NumberFormatException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("value must be a number");
        }
        catch (DataAccessResourceFailureException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connection failed");
        }
        catch (CannotCreateTransactionException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database is currently unavailable");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Integer id, @Valid @RequestBody ProjectRequest request) {
        try{
            ProjectResponse projectResponse = projectService.updateProject(id, request);
            return ResponseEntity.ok(projectResponse);
        }
        catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Project already exists");
        }
        catch (CannotCreateTransactionException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database is currently unavailable");
        }
        catch (DataAccessResourceFailureException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connection failed");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
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
        try {
            int deletedCount = projectService.deleteProjectByCriteria(criteria, value);
            if (deletedCount == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No projects found");
            }
            return ResponseEntity.ok("Deleted " + deletedCount + " project(s).");
        }
        catch (NumberFormatException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("value must be a number");
        }
        catch (CannotCreateTransactionException | DataAccessResourceFailureException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connection failed");
        }
    }

}
