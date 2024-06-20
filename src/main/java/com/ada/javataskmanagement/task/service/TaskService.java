package com.ada.javataskmanagement.task.service;

import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.project.repository.ProjectRepository;
import com.ada.javataskmanagement.project.service.ProjectService;
import com.ada.javataskmanagement.task.dto.TaskDTO;
import com.ada.javataskmanagement.task.model.Task;
import com.ada.javataskmanagement.task.model.TaskStatus;
import com.ada.javataskmanagement.task.repository.TaskRepository;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.worker.service.WorkerService;
import com.ada.javataskmanagement.workerproject.repository.WorkerProjectRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final WorkerProjectRepository workerProjectRepository;

    private final WorkerService workerService;

    private final Clock clock;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, WorkerProjectRepository workerProjectRepository, WorkerService workerService, ProjectService projectService, Clock clock) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.workerProjectRepository = workerProjectRepository;
        this.workerService = workerService;
        this.clock = clock;
    }

    private void validateWorkerInProject(UUID projectId, UUID workerId) {
        boolean exists = workerProjectRepository.existsByWorkerUuidAndProjectUuid(workerId, projectId);
        if (!exists) {
            throw new IllegalArgumentException("Worker does not belong to this project");
        }
    }

    public Task createTask(Task task) {
        if (task.getProject() == null || task.getProject().getUuid() == null) {
            throw new IllegalArgumentException("Project ID must be provided");
        }
        if (task.getShortDescription() != null && task.getShortDescription().length() > 100) {
            throw new IllegalArgumentException("Short description must be 100 characters or less");
        }
        validateWorkerInProject(task.getProject().getUuid(), task.getCreatedBy().getUuid());
        Worker worker = workerService.findWorkerById(task.getCreatedBy().getUuid());
        task.setCreatedBy(worker);
        validateTaskDate(task.getDeadline());

        return taskRepository.save(task);
    }

    private void validateTaskDate(LocalDate deadline) {
        LocalDate currentDate = LocalDate.now(clock);
        if (deadline != null && deadline.isBefore(currentDate)) {
            throw new IllegalArgumentException("Deadline cannot be in the past");
        }
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

    public Task assignWorker(UUID taskId, UUID workerId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Worker worker = workerService.findWorkerById(workerId);
        validateWorkerInProject(task.getProject().getUuid(), workerId);
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

