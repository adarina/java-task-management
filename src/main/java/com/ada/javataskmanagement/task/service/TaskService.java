package com.ada.javataskmanagement.task.service;

import com.ada.javataskmanagement.project.repository.ProjectRepository;
import com.ada.javataskmanagement.task.model.Task;
import com.ada.javataskmanagement.task.model.TaskStatus;
import com.ada.javataskmanagement.task.repository.TaskRepository;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.worker.repository.WorkerRepository;
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
    private final WorkerRepository workerRepository;
    private final WorkerProjectRepository workerProjectRepository;

    private final WorkerService workerService;

    private final Clock clock;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, WorkerRepository workerRepository, WorkerProjectRepository workerProjectRepository, WorkerService workerService, Clock clock) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.workerRepository = workerRepository;
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

    public Task createTask(Task task, UUID workerId) {
        if (task.getProject() == null || task.getProject().getUuid() == null) {
            throw new IllegalArgumentException("Project ID must be provided");
        }
        if (task.getShortDescription() != null && task.getShortDescription().length() > 100) {
            throw new IllegalArgumentException("Short description must be 100 characters or less");
        }
        validateWorkerInProject(task.getProject().getUuid(), workerId);
        Worker worker = workerService.findWorkerById(workerId);
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
}

