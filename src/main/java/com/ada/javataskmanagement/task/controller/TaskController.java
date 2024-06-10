package com.ada.javataskmanagement.task.controller;

import com.ada.javataskmanagement.task.model.Task;
import com.ada.javataskmanagement.task.model.TaskStatus;
import com.ada.javataskmanagement.task.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task, @RequestParam UUID workerId) {
        Task createdTask = taskService.createTask(task, workerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PostMapping("/{taskId}/set-default-status")
    public ResponseEntity<Task> setDefaultStatus(@PathVariable UUID taskId) {
        Task updatedTask = taskService.setDefaultStatus(taskId, TaskStatus.TO_DO);
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping("/{taskId}/set-priority")
    public ResponseEntity<Task> setPriority(@PathVariable UUID taskId, @RequestParam TaskStatus status) {
        Task updatedTask = taskService.setPriority(taskId, status);
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping("/{taskId}/add-description")
    public ResponseEntity<Task> addDescription(@PathVariable UUID taskId, @RequestParam String description) {
        Task updatedTask = taskService.addDescription(taskId, description);
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping("/{taskId}/set-status")
    public ResponseEntity<Task> setStatus(@PathVariable UUID taskId, @RequestParam TaskStatus status) {
        Task updatedTask = taskService.setStatus(taskId, status);
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping("/{taskId}/assign-worker")
    public ResponseEntity<Task> assignWorker(@PathVariable UUID taskId, @RequestParam UUID workerId) {
        Task updatedTask = taskService.assignWorker(taskId, workerId);
        return ResponseEntity.ok(updatedTask);
    }
}
