package com.ada.javataskmanagement.worker.validation;

import com.ada.javataskmanagement.task.validation.DeadlineTaskValidator;
import com.ada.javataskmanagement.worker.model.Worker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class EmailWorkerValidator extends WorkerValidator {

    private static final Logger logger = LoggerFactory.getLogger(DeadlineTaskValidator.class);

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(.+)$");

    @Override
    public boolean check(Worker worker) {
        if (StringUtils.isBlank(worker.getEmail())) {
            logger.error("Worker validation failed: Email is blank.");
            return false;
        } else if (!EMAIL_PATTERN.matcher(worker.getEmail()).matches()) {
            logger.error("Worker validation failed: Invalid email format - " + worker.getEmail());
            return false;
        }
        return checkNext(worker);
    }
}
