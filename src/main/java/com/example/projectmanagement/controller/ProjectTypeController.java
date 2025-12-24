package com.example.projectmanagement.controller;

import com.example.projectmanagement.dto.ProjectTypeRequest;
import com.example.projectmanagement.dto.ProjectTypeResponse;
import com.example.projectmanagement.exception.ResourceNotFoundException;
import com.example.projectmanagement.service.ProjectTypeService;
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
@RequestMapping("/api/projectTypes")
public class ProjectTypeController {

    private final ProjectTypeService projectTypeService;

    public ProjectTypeController(ProjectTypeService projectTypeService){
        this.projectTypeService = projectTypeService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createProjectType(@Valid @RequestBody ProjectTypeRequest request){
        try{
            ProjectTypeResponse projectTypeResponse = projectTypeService.createProjectType(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(projectTypeResponse);
        }
        catch (DataIntegrityViolationException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Project type with the same name already exists");
        }
        catch (CannotCreateTransactionException | DataAccessResourceFailureException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connection failed");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectTypeById(@PathVariable Integer id){
        try{
            ProjectTypeResponse projectTypeResponse = projectTypeService.getProjectTypeById(id);
            return ResponseEntity.status(HttpStatus.OK).body(projectTypeResponse);
        }
        catch (ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        catch (CannotCreateTransactionException | DataAccessResourceFailureException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connection failed");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<?> getAllProjectType(){
        try{
            List<ProjectTypeResponse> projectTypeResponse = projectTypeService.getAllProjectType();
            return ResponseEntity.ok(projectTypeResponse);
        }
        catch (CannotCreateTransactionException | DataAccessResourceFailureException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connection failed");
        }
    }
}
