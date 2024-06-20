package com.ada.javataskmanagement.test.service;

import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.project.repository.ProjectRepository;
import com.ada.javataskmanagement.task.model.Task;
import com.ada.javataskmanagement.task.model.TaskStatus;
import com.ada.javataskmanagement.task.repository.TaskRepository;
import com.ada.javataskmanagement.task.service.TaskService;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.worker.service.WorkerService;
import com.ada.javataskmanagement.workerproject.model.WorkerProject;
import com.ada.javataskmanagement.workerproject.repository.WorkerProjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@SpringBootTest
@Transactional
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private WorkerProjectRepository workerProjectRepository;

    private Worker defaultWorker;
    private Project defaultProject;

    @BeforeEach
    public void setUp() {
        defaultWorker = new Worker();
        defaultWorker.setFirstname("Marek");
        defaultWorker.setLastname("Mostowiak");
        defaultWorker.setEmail("mm@sth.com");
        defaultWorker = workerService.addWorker(defaultWorker);

        defaultProject = new Project();
        defaultProject.setName("Default Project");
        defaultProject.setDescription("Description of the default project.");
        defaultProject = projectRepository.save(defaultProject);

        workerProjectRepository.save(new WorkerProject(defaultWorker, defaultProject));
    }

    @Test
    public void should_CreateTask_When_ValidTask() {

        Task task = new Task();
        task.setTitle("Task");
        task.setShortDescription("Short description");
        task.setLongDescription("Long description");
        task.setDeadline(LocalDate.now(Clock.system(ZoneId.of("UTC"))).plusDays(5));
        task.setCreatedBy(defaultWorker);
        task.setProject(defaultProject);

        Task savedTask = taskService.createTask(task);

        Assertions.assertNotNull(savedTask.getUuid());
        Assertions.assertEquals("Task", savedTask.getTitle());
        Assertions.assertEquals("Short description", savedTask.getShortDescription());
        Assertions.assertEquals("Long description", savedTask.getLongDescription());
        Assertions.assertEquals(LocalDate.now(Clock.system(ZoneId.of("UTC"))).plusDays(5), savedTask.getDeadline());
        Assertions.assertEquals(defaultWorker.getUuid(), savedTask.getCreatedBy().getUuid());
        Assertions.assertEquals(defaultProject.getUuid(), savedTask.getProject().getUuid());
    }

    @Test
    public void should_ThrowException_When_CreateTaskWithInvalidProject() {
        Task task = new Task();
        task.setTitle("Sample Task");
        task.setShortDescription("Short description.");
        task.setLongDescription("Long description");
        task.setDeadline(LocalDate.now(Clock.system(ZoneId.of("UTC"))).plusDays(5));
        task.setCreatedBy(defaultWorker);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(task);
        });
    }

    @Test
    public void should_ThrowException_When_CreateTaskWithLongDescription() {

        Task task = new Task();
        task.setTitle("Task");
        task.setShortDescription("This is a very long short description that exceeds one hundred characters and should therefore throw an exception.");
        task.setLongDescription("This is a long description");
        task.setDeadline(LocalDate.now(Clock.system(ZoneId.of("UTC"))).plusDays(5));
        task.setCreatedBy(defaultWorker);
        task.setProject(defaultProject);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(task);
        });
    }

    @Test
    public void should_ThrowException_When_CreateTaskWithPastDeadline() {

        Task task = new Task();
        task.setTitle("Task");
        task.setShortDescription("Short description.");
        task.setLongDescription("Long description");
        task.setDeadline(LocalDate.now(Clock.system(ZoneId.of("UTC"))).minusDays(1));
        task.setCreatedBy(defaultWorker);
        task.setProject(defaultProject);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(task);
        });
    }

    @Test
    public void should_SetTaskStatus_When_ValidTaskStatus() {
        Task task = new Task();
        task.setTitle("Task");
        task.setShortDescription("Short description.");
        task.setLongDescription("Long description");
        task.setDeadline(LocalDate.now(Clock.system(ZoneId.of("UTC"))).plusDays(5));
        task.setCreatedBy(defaultWorker);
        task.setProject(defaultProject);
        Task savedTask = taskRepository.save(task);

        Task updatedTask = taskService.setStatus(savedTask.getUuid(), TaskStatus.IN_PROGRESS);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, updatedTask.getStatus());
    }

    @Test
    public void should_AssignWorkerToTask_When_ValidWorkerAndTask() {

        Task task = new Task();
        task.setTitle("Task");
        task.setShortDescription("Short description.");
        task.setLongDescription("Long description");
        task.setDeadline(LocalDate.now(Clock.system(ZoneId.of("UTC"))).plusDays(5));
        task.setCreatedBy(defaultWorker);
        task.setProject(defaultProject);
        Task savedTask = taskRepository.save(task);

        Task updatedTask = taskService.assignWorker(savedTask.getUuid(), defaultWorker.getUuid());

        Assertions.assertEquals(defaultWorker.getUuid(), updatedTask.getAssignTo().getUuid());
    }
}
