package com.ada.javataskmanagement.task.controller;

import com.ada.javataskmanagement.task.dto.TaskDTO;
import com.ada.javataskmanagement.task.dto.TaskStatusDTO;
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
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        Task task = taskService.convertToEntity(taskDTO);
        Task createdTask = taskService.createTask(task);
        TaskDTO createdTaskDTO = taskService.convertToDTO(createdTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskDTO);
    }

    @PostMapping("/{taskId}/set-default-status")
    public ResponseEntity<TaskDTO> setDefaultStatus(@PathVariable UUID taskId) {
        Task updatedTask = taskService.setDefaultStatus(taskId, TaskStatus.TO_DO);
        TaskDTO updatedTaskDTO = taskService.convertToDTO(updatedTask);
        return ResponseEntity.ok(updatedTaskDTO);
    }

    @PostMapping("/{taskId}/set-priority")
    public ResponseEntity<TaskDTO> setPriority(@PathVariable UUID taskId, @RequestBody TaskStatusDTO statusDTO) {
        TaskStatus status = TaskStatus.valueOf(statusDTO.getStatus());
        Task updatedTask = taskService.setPriority(taskId, status);
        TaskDTO updatedTaskDTO = taskService.convertToDTO(updatedTask);
        return ResponseEntity.ok(updatedTaskDTO);
    }

    @PostMapping("/{taskId}/add-description")
    public ResponseEntity<TaskDTO> addDescription(@PathVariable UUID taskId, @RequestBody String description) {
        Task updatedTask = taskService.addDescription(taskId, description);
        TaskDTO updatedTaskDTO = taskService.convertToDTO(updatedTask);
        return ResponseEntity.ok(updatedTaskDTO);
    }

    @PostMapping("/{taskId}/set-status")
    public ResponseEntity<TaskDTO> setStatus(@PathVariable UUID taskId, @RequestBody TaskStatusDTO statusDTO) {
        TaskStatus status = TaskStatus.valueOf(statusDTO.getStatus());
        Task updatedTask = taskService.setStatus(taskId, status);
        TaskDTO updatedTaskDTO = taskService.convertToDTO(updatedTask);
        return ResponseEntity.ok(updatedTaskDTO);
    }

    @PostMapping("/{taskId}/assign-worker/{workerId}")
    public ResponseEntity<TaskDTO> assignWorker(@PathVariable UUID taskId, @PathVariable UUID workerId) {
        Task updatedTask = taskService.assignWorker(taskId, workerId);
        TaskDTO updatedTaskDTO = taskService.convertToDTO(updatedTask);
        return ResponseEntity.ok(updatedTaskDTO);
    }
}
