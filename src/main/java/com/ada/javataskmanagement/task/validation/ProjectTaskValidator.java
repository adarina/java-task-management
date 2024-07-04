package com.ada.javataskmanagement.task.validation;

import com.ada.javataskmanagement.task.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectTaskValidator extends TaskValidator {

    private static final Logger logger = LoggerFactory.getLogger(ProjectTaskValidator.class);

    @Override
    public boolean check(Task task) {
        if (task.getProject() == null) {
            logger.error("Task validation failed: Task project is null.");
            return false;
        } else if (task.getProject().getUuid() == null) {
            logger.error("Task validation failed: Task project's UUID is null.");
            return false;
        }
        return checkNext(task);
    }
}
