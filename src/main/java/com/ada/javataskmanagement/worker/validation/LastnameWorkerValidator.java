package com.ada.javataskmanagement.worker.validation;

import com.ada.javataskmanagement.task.validation.DeadlineTaskValidator;
import com.ada.javataskmanagement.worker.model.Worker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class LastnameWorkerValidator extends WorkerValidator {

    private static final Logger logger = LoggerFactory.getLogger(DeadlineTaskValidator.class);

    private static final Pattern SURNAME_PATTERN = Pattern.compile("[A-Za-z]+");

    @Override
    public boolean check(Worker worker) {
        if (StringUtils.isBlank(worker.getLastname())) {
            logger.error("Worker validation failed: Last name is blank.");
            return false;
        } else if (!SURNAME_PATTERN.matcher(worker.getLastname()).matches()) {
            logger.error("Worker validation failed: Invalid last name format - " + worker.getLastname());
            return false;
        }
        return checkNext(worker);
    }
}
