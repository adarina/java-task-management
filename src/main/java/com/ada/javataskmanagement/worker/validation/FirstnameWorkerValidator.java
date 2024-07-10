package com.ada.javataskmanagement.worker.validation;

import com.ada.javataskmanagement.task.validation.DeadlineTaskValidator;
import com.ada.javataskmanagement.worker.model.Worker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

@Slf4j
public class FirstnameWorkerValidator extends WorkerValidator {

    private static final Pattern NAME_PATTERN = Pattern.compile("[A-Za-z]+");

    @Override
    public boolean check(Worker worker) {
        if (StringUtils.isBlank(worker.getFirstname())) {
            log.error("Worker validation failed: First name is blank.");
            return false;
        } else if (!NAME_PATTERN.matcher(worker.getFirstname()).matches()) {
            log.error("Worker validation failed: Invalid first name format - " + worker.getFirstname());
            return false;
        }
        return checkNext(worker);
    }
}
