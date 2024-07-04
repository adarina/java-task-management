package com.ada.javataskmanagement.task.validation;

import com.ada.javataskmanagement.task.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDate;

public class DeadlineTaskValidator extends TaskValidator {

    private static final Logger logger = LoggerFactory.getLogger(DeadlineTaskValidator.class);
    private final Clock clock;

    public DeadlineTaskValidator(Clock clock) {
        this.clock = clock;
    }

    @Override
    public boolean check(Task task) {
        LocalDate currentDate = LocalDate.now(clock);
        if (task.getDeadline() != null && task.getDeadline().isBefore(currentDate)) {
            logger.error("Task validation failed: Task deadline {} is before current date {}.", task.getDeadline(), currentDate);
            return false;
        }
        return checkNext(task);
    }
}
