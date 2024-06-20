package com.ada.javataskmanagement.project.controller;

import com.ada.javataskmanagement.project.dto.ProjectDTO;
import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.project.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/{projectId}/add-worker/{workerId}")
    public ResponseEntity<ProjectDTO> addWorkerToProject(@PathVariable UUID projectId, @PathVariable UUID workerId) {
        Project project = projectService.addWorkerToProject(projectId, workerId);
        ProjectDTO projectDTO = projectService.convertToDTO(project);
        return ResponseEntity.ok(projectDTO);
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> addProject(@RequestBody ProjectDTO projectDTO) {
        Project project = projectService.convertToEntity(projectDTO);
        Project savedProject = projectService.addProject(project);
        ProjectDTO savedProjectDTO = projectService.convertToDTO(savedProject);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProjectDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable UUID id) {
        Project project = projectService.findProjectById(id);
        if (project != null) {
            ProjectDTO projectDTO = projectService.convertToDTO(project);
            return ResponseEntity.ok(projectDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(projectService::convertToDTO)
                .toList();
        return ResponseEntity.ok(projectDTOs);
    }
}