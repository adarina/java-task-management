package com.ada.javataskmanagement.worker.validation;

import com.ada.javataskmanagement.task.validation.DeadlineTaskValidator;
import com.ada.javataskmanagement.worker.model.Worker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

@Slf4j
public class LastnameWorkerValidator extends WorkerValidator {

    private static final Pattern SURNAME_PATTERN = Pattern.compile("[A-Za-z]+");

    @Override
    public boolean check(Worker worker) {
        if (StringUtils.isBlank(worker.getLastname())) {
            log.error("Worker validation failed: Last name is blank.");
            return false;
        } else if (!SURNAME_PATTERN.matcher(worker.getLastname()).matches()) {
            log.error("Worker validation failed: Invalid last name format - " + worker.getLastname());
            return false;
        }
        return checkNext(worker);
    }
}
