package com.ada.javataskmanagement.project.service;

import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.project.repository.ProjectRepository;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.worker.service.WorkerService;
import com.ada.javataskmanagement.workerproject.model.WorkerProject;
import com.ada.javataskmanagement.workerproject.repository.WorkerProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final WorkerService workerService;
    private final WorkerProjectRepository workerProjectRepository;

    public ProjectService(ProjectRepository projectRepository, WorkerService workerService, WorkerProjectRepository workerProjectRepository) {
        this.projectRepository = projectRepository;
        this.workerService = workerService;
        this.workerProjectRepository = workerProjectRepository;
    }

    public Project addWorkerToProject(UUID projectId, UUID workerId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException("Project not found"));
        Worker worker = workerService.findWorkerById(workerId);

        if (worker != null) {
            boolean alreadyAssigned = workerProjectRepository.existsByWorkerUuidAndProjectUuid(workerId, projectId);
            if (!alreadyAssigned) {
                WorkerProject workerProject = new WorkerProject();
                workerProject.setWorker(worker);
                workerProject.setProject(project);
                workerProjectRepository.save(workerProject);
            }
            return project;
        } else {
            throw new IllegalArgumentException("Worker not found");
        }
    }

    public Project addProject(Project project) {
        return projectRepository.save(project);
    }

    public Project findProjectById(UUID id) {
        return projectRepository.findById(id).orElse(null);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
}
