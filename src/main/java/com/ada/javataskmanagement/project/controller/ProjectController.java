package com.ada.javataskmanagement.project.controller;

import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.project.service.ProjectService;
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

    @PostMapping("/{projectId}/add-worker")
    public Project addWorkerToProject(@PathVariable UUID projectId, @RequestParam UUID workerId) {
        return projectService.addWorkerToProject(projectId, workerId);
    }

    @PostMapping
    public Project addProject(@RequestBody Project project) {
        return projectService.addProject(project);
    }

    @GetMapping("/{id}")
    public Project getProject(@PathVariable UUID id) {
        return projectService.findProjectById(id);
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }
}
