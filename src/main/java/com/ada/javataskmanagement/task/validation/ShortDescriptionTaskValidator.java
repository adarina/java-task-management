package com.ada.javataskmanagement.task.validation;

import com.ada.javataskmanagement.task.model.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShortDescriptionTaskValidator extends TaskValidator {

    private static final Logger logger = LoggerFactory.getLogger(ShortDescriptionTaskValidator.class);

    @Override
    public boolean check(Task task) {
        if (StringUtils.isBlank(task.getShortDescription())) {
            logger.error("Task validation failed: Short description is blank.");
            return false;
        } else if (task.getShortDescription().length() > 100) {
            logger.error("Task validation failed: Short description exceeds 100 characters. Length: {}", task.getShortDescription().length());
            return false;
        }
        return checkNext(task);
    }
}