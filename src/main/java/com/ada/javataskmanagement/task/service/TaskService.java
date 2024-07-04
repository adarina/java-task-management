package com.ada.javataskmanagement.task.service;

import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.project.repository.ProjectRepository;
import com.ada.javataskmanagement.project.service.ProjectService;
import com.ada.javataskmanagement.task.dto.TaskDTO;
import com.ada.javataskmanagement.task.model.Task;
import com.ada.javataskmanagement.task.model.TaskStatus;
import com.ada.javataskmanagement.task.repository.TaskRepository;
import com.ada.javataskmanagement.task.validation.*;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.worker.service.WorkerService;
import com.ada.javataskmanagement.workerproject.repository.WorkerProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.UUID;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final WorkerProjectRepository workerProjectRepository;
    private final WorkerService workerService;
    private final Clock clock;
    private final TaskValidator taskValidator;


    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, WorkerProjectRepository workerProjectRepository, WorkerService workerService, ProjectService projectService, Clock clock) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.workerProjectRepository = workerProjectRepository;
        this.workerService = workerService;
        this.clock = clock;

        this.taskValidator = TaskValidator.link(
                new ShortDescriptionTaskValidator(),
                new ProjectTaskValidator(),
                new DeadlineTaskValidator(clock),
                new WorkerInProjectTaskValidator(workerProjectRepository)
        );
    }

    public boolean validateTask(Task task) {
        boolean isValid = taskValidator.check(task);
        if (!isValid) {
            logger.error("Task validation failed: {}", task);
        }
        return isValid;
    }

    public Task createTask(Task task) {

        if (!validateTask(task)) {
            throw new IllegalArgumentException("Task validation failed");
        }
        return taskRepository.save(task);
    }

    public Task setDefaultStatus(UUID taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setStatus(status);
        return taskRepository.save(task);
    }

    public Task setPriority(UUID taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setStatus(status);
        return taskRepository.save(task);
    }

    public Task addDescription(UUID taskId, String description) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setLongDescription(description);
        return taskRepository.save(task);
    }

    public Task setStatus(UUID taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setStatus(status);
        return taskRepository.save(task);
    }

    private void validateWorkerInProject(UUID projectId, UUID workerId) {
        boolean exists = workerProjectRepository.existsByWorkerUuidAndProjectUuid(workerId, projectId);
        if (!exists) {
            throw new IllegalArgumentException("Worker does not belong to this project.");
        }
    }

    public Task assignWorker(UUID taskId, UUID workerId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Worker worker = workerService.findWorkerById(workerId);
        if (worker == null) {
            throw new IllegalArgumentException("Worker not found");
        }
        validateWorkerInProject(task.getProject().getUuid(), worker.getUuid());
        task.setAssignTo(worker);
        return taskRepository.save(task);
    }

    public TaskDTO convertToDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle(task.getTitle());
        taskDTO.setShortDescription(task.getShortDescription());
        taskDTO.setLongDescription(task.getLongDescription());
        taskDTO.setDeadline(task.getDeadline());
        taskDTO.setCreatedById(task.getCreatedBy() != null ? task.getCreatedBy().getUuid() : null);
        taskDTO.setAssignToId(task.getAssignTo() != null ? task.getAssignTo().getUuid() : null);
        taskDTO.setProjectId(task.getProject() != null ? task.getProject().getUuid() : null);
        taskDTO.setStatus(task.getStatus().name());
        return taskDTO;
    }

    public Task convertToEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setShortDescription(taskDTO.getShortDescription());
        task.setLongDescription(taskDTO.getLongDescription());
        task.setDeadline(taskDTO.getDeadline());
        if (taskDTO.getCreatedById() != null) {
            Worker createdBy = workerService.findWorkerById(taskDTO.getCreatedById());
            task.setCreatedBy(createdBy);
        }
        if (taskDTO.getAssignToId() != null) {
            Worker assignTo = workerService.findWorkerById(taskDTO.getAssignToId());
            task.setAssignTo(assignTo);
        }
        if (taskDTO.getProjectId() != null) {
            Project project = projectRepository.findById(taskDTO.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("Project not found"));
            task.setProject(project);
        }
        if (taskDTO.getStatus() != null) {
            task.setStatus(TaskStatus.valueOf(taskDTO.getStatus()));
        }
        return task;
    }
}

