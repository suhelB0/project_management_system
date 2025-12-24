package com.example.projectmanagement.controller;

import com.example.projectmanagement.dto.ProjectSubTypeRequest;
import com.example.projectmanagement.dto.ProjectSubTypeResponse;
import com.example.projectmanagement.exception.ResourceNotFoundException;
import com.example.projectmanagement.service.ProjectSubTypeService;
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
@RequestMapping("/api/projectSubType")
public class ProjectSubTypeController {

    private final ProjectSubTypeService projectSubTypeService;

    public ProjectSubTypeController(ProjectSubTypeService projectSubTypeService) {
        this.projectSubTypeService = projectSubTypeService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createProjectSubType(@Valid @RequestBody ProjectSubTypeRequest request) {
        try{
            ProjectSubTypeResponse projectSubTypeResponse= projectSubTypeService.createProjectSubType(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(projectSubTypeResponse);
        }
        catch (DataIntegrityViolationException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Project type with the same name already exists");
        }
        catch (CannotCreateTransactionException | DataAccessResourceFailureException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connection failed");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<?> getAllProjectSubType() {
        try{
            List<ProjectSubTypeResponse> projectSubTypeResponse = projectSubTypeService.getAllProjectSubType();
            return ResponseEntity.status(HttpStatus.OK).body(projectSubTypeResponse);
        }
        catch (CannotCreateTransactionException | DataAccessResourceFailureException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connection failed");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectSubTypeById(@PathVariable Integer id) {
        try{
            ProjectSubTypeResponse projectSubTypeResponse = projectSubTypeService.getProjectSubTypeById(id);
            return ResponseEntity.status(HttpStatus.OK).body(projectSubTypeResponse);
        }
        catch (ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        catch (CannotCreateTransactionException | DataAccessResourceFailureException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connection failed");
        }
    }
}
