package com.ada.javataskmanagement.project.service;

import com.ada.javataskmanagement.project.dto.ProjectDTO;
import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.project.repository.ProjectRepository;
import com.ada.javataskmanagement.project.validation.NameProjectValidator;
import com.ada.javataskmanagement.project.validation.ProjectValidator;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.project.validation.AssignedWorkerValidator;

import com.ada.javataskmanagement.worker.service.WorkerService;
import com.ada.javataskmanagement.workerproject.model.WorkerProject;
import com.ada.javataskmanagement.workerproject.repository.WorkerProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final WorkerService workerService;
    private final WorkerProjectRepository workerProjectRepository;
    private final ProjectValidator projectValidator;

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);


    public ProjectService(ProjectRepository projectRepository, WorkerService workerService, WorkerProjectRepository workerProjectRepository) {
        this.projectRepository = projectRepository;
        this.workerService = workerService;
        this.workerProjectRepository = workerProjectRepository;
        this.projectValidator = ProjectValidator.link(
                new NameProjectValidator(),
                new AssignedWorkerValidator(workerProjectRepository));
    }

    public boolean validateProject(Project project, Worker worker) {
        boolean isValid = projectValidator.check(project, worker);
        if (!isValid) {
            logger.error("Project validation failed: {}", project);
        }
        return isValid;
    }

    public Project addWorkerToProject(UUID projectId, UUID workerId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException("Project not found"));
        Worker worker = workerService.findWorkerById(workerId);

        if (worker != null) {
            if (!validateProject(project, worker)) {
                throw new IllegalArgumentException("Project validation failed.");
            }
            WorkerProject workerProject = new WorkerProject();
            workerProject.setWorker(worker);
            workerProject.setProject(project);
            workerProjectRepository.save(workerProject);
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

    public ProjectDTO convertToDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setUuid(project.getUuid());
        projectDTO.setName(project.getName());
        projectDTO.setDescription(project.getDescription());
        return projectDTO;
    }

    public Project convertToEntity(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        return project;
    }
}
