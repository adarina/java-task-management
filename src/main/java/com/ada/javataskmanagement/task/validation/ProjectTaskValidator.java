package com.ada.javataskmanagement.task.validation;

import com.ada.javataskmanagement.task.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class ProjectTaskValidator extends TaskValidator {

    @Override
    public boolean check(Task task) {
        if (task.getProject() == null) {
            log.error("Task validation failed: Task project is null.");
            return false;
        } else if (task.getProject().getUuid() == null) {
            log.error("Task validation failed: Task project's UUID is null.");
            return false;
        }
        return checkNext(task);
    }
}
