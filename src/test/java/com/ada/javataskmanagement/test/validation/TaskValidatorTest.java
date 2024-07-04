package com.ada.javataskmanagement.test.validation;

import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.task.model.Task;
import com.ada.javataskmanagement.task.validation.DeadlineTaskValidator;
import com.ada.javataskmanagement.task.validation.ProjectTaskValidator;
import com.ada.javataskmanagement.task.validation.ShortDescriptionTaskValidator;
import com.ada.javataskmanagement.task.validation.TaskValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class TaskValidatorTest {

    TaskValidator taskValidator;

    Clock clock;

    @BeforeEach
    void setUp() {

        clock = Clock.fixed(
                Instant.parse("2024-07-01T10:00:00Z"),
                ZoneId.systemDefault()
        );

        taskValidator = TaskValidator.link(
                new ProjectTaskValidator(),
                new ShortDescriptionTaskValidator(),
                new DeadlineTaskValidator(clock)
        );
    }

    @Test
    void shouldFailWhenProjectIsNull() {
        Task task = new Task();
        task.setProject(null);

        assertFalse(taskValidator.check(task));
    }

    @Test
    void shouldFailWhenProjectUUIDIsNull() {
        Task task = new Task();
        Project project = new Project();
        project.setUuid(null);
        task.setProject(project);

        assertFalse(taskValidator.check(task));
    }

    @Test
    void shouldFailWhenShortDescriptionIsBlank() {
        UUID projectUuid = UUID.randomUUID();
        Project project = new Project();
        project.setUuid(projectUuid);

        Task task = new Task();
        task.setProject(project);

        assertFalse(taskValidator.check(task));
    }

    @Test
    void shouldFailWhenShortDescriptionHasMoreCharsThan100() {
        UUID projectUuid = UUID.randomUUID();
        Project project = new Project();
        project.setUuid(projectUuid);

        Task task = new Task();
        task.setProject(project);
        task.setShortDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

        assertFalse(taskValidator.check(task));
    }

    @Test
    void shouldFailWhenDeadlineIsBeforeCurrentDate() {
        UUID projectUuid = UUID.randomUUID();
        Project project = new Project();
        project.setUuid(projectUuid);

        Task task = new Task();
        task.setProject(project);
        task.setShortDescription("Lorem ipsum dolor sit amet.");
        task.setDeadline(LocalDate.now(clock).minusDays(5));

        assertFalse(taskValidator.check(task));
    }
}
