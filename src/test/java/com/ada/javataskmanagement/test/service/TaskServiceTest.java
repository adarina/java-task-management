package com.ada.javataskmanagement.test.service;

import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.project.repository.ProjectRepository;
import com.ada.javataskmanagement.task.model.Task;
import com.ada.javataskmanagement.task.model.TaskStatus;
import com.ada.javataskmanagement.task.service.TaskService;
import com.ada.javataskmanagement.task.repository.TaskRepository;
import com.ada.javataskmanagement.task.validation.*;
import com.ada.javataskmanagement.worker.service.WorkerService;
import com.ada.javataskmanagement.workerproject.repository.WorkerProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private WorkerProjectRepository workerProjectRepository;

    @Mock
    private WorkerService workerService;

    @Mock
    private Clock clock;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        TaskValidator taskValidator = TaskValidator.link(
                new ShortDescriptionTaskValidator(),
                new ProjectTaskValidator(),
                new DeadlineTaskValidator(clock),
                new WorkerInProjectTaskValidator(workerProjectRepository)
        );
        taskService = new TaskService(taskRepository, projectRepository, workerProjectRepository, workerService, null, clock);
    }

    @Test
    void shouldThrowExceptionWhenTaskValidationFails() {
        Task task = new Task();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskService.createTask(task));

        assertEquals("Task validation failed", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTaskIdIsNullForSetDefaultStatus() {
        UUID taskId = null;
        TaskStatus status = TaskStatus.TO_DO;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskService.setDefaultStatus(taskId, status));

        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFoundForSetDefaultStatus() {
        UUID taskId = UUID.randomUUID();
        TaskStatus status = TaskStatus.TO_DO;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskService.setDefaultStatus(taskId, status));

        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFoundForSetPriority() {
        UUID taskId = UUID.randomUUID();
        TaskStatus status = TaskStatus.IN_PROGRESS;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskService.setPriority(taskId, status));

        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFoundForAddDescription() {
        UUID taskId = UUID.randomUUID();
        String description = "Detailed task description";

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskService.addDescription(taskId, description));

        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFoundForSetStatus() {
        UUID taskId = UUID.randomUUID();
        TaskStatus status = TaskStatus.DONE;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskService.setStatus(taskId, status));

        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFoundForAssignWorker() {
        UUID taskId = UUID.randomUUID();
        UUID workerId = UUID.randomUUID();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskService.assignWorker(taskId, workerId));

        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenWorkerNotFoundForAssignWorker() {
        UUID taskId = UUID.randomUUID();
        UUID workerId = UUID.randomUUID();
        Task task = new Task();
        Project project = new Project();
        project.setUuid(UUID.randomUUID());
        task.setProject(project);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(workerService.findWorkerById(workerId)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskService.assignWorker(taskId, workerId));

        assertEquals("Worker not found", exception.getMessage());
    }
}
