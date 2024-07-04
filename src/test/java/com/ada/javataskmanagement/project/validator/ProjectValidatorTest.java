package com.ada.javataskmanagement.project.validator;

import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.project.validation.AssignedWorkerValidator;
import com.ada.javataskmanagement.project.validation.NameProjectValidator;
import com.ada.javataskmanagement.project.validation.ProjectValidator;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.workerproject.repository.WorkerProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ProjectValidatorTest {

    private ProjectValidator projectValidator;

    private WorkerProjectRepository workerProjectRepository;

    @BeforeEach
    void setUp() {
        workerProjectRepository = Mockito.mock(WorkerProjectRepository.class);
        projectValidator = ProjectValidator.link(
                new NameProjectValidator(),
                new AssignedWorkerValidator(workerProjectRepository)
        );
    }

    @Test
    void shouldFailWhenProjectNameIsBlank() {
        Project project = new Project();
        project.setName("");

        Worker worker = new Worker();
        UUID workerUuid = UUID.randomUUID();
        worker.setUuid(workerUuid);

        assertFalse(projectValidator.check(project, worker));
    }

    @Test
    void shouldFailWhenProjectNameContainsInvalidCharacters() {
        Project project = new Project();
        project.setName("ValidName$");

        Worker worker = new Worker();
        UUID workerUuid = UUID.randomUUID();
        worker.setUuid(workerUuid);

        assertFalse(projectValidator.check(project, worker));
    }

    @Test
    void shouldPassWhenProjectNameIsValid() {
        Project project = new Project();
        project.setName("ValidName");

        Worker worker = new Worker();
        UUID workerUuid = UUID.randomUUID();
        worker.setUuid(workerUuid);

        assertTrue(projectValidator.check(project, worker));
    }

    @Test
    void shouldFailWhenWorkerIsAlreadyAssignedToProject() {
        Project project = new Project();
        project.setName("ValidName");
        UUID projectUuid = UUID.randomUUID();
        project.setUuid(projectUuid);

        Worker worker = new Worker();
        UUID workerUuid = UUID.randomUUID();
        worker.setUuid(workerUuid);

        when(workerProjectRepository.existsByWorkerUuidAndProjectUuid(workerUuid, projectUuid))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectValidator.check(project, worker);
        });

        assertTrue(exception.getMessage().contains("Worker is already assigned to the project."));
    }


    @Test
    void shouldPassWhenWorkerIsNotAssignedToProject() {
        Project project = new Project();
        project.setName("ValidProjectName");
        UUID projectUuid = UUID.randomUUID();
        project.setUuid(projectUuid);

        Worker worker = new Worker();
        UUID workerUuid = UUID.randomUUID();
        worker.setUuid(workerUuid);

        when(workerProjectRepository.existsByWorkerUuidAndProjectUuid(workerUuid, projectUuid))
                .thenReturn(false);

        assertTrue(projectValidator.check(project, worker));
    }
}

