package com.ada.javataskmanagement.task.validation;

import com.ada.javataskmanagement.task.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class ShortDescriptionTaskValidator extends TaskValidator {

    @Override
    public boolean check(Task task) {
        if (StringUtils.isBlank(task.getShortDescription())) {
            log.error("Task validation failed: Short description is blank.");
            return false;
        } else if (task.getShortDescription().length() > 100) {
            log.error("Task validation failed: Short description exceeds 100 characters. Length: {}", task.getShortDescription().length());
            return false;
        }
        return checkNext(task);
    }
}