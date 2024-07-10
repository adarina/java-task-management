package com.ada.javataskmanagement.worker.validation;

import com.ada.javataskmanagement.task.validation.DeadlineTaskValidator;
import com.ada.javataskmanagement.worker.model.Worker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

@Slf4j
public class EmailWorkerValidator extends WorkerValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(.+)$");

    @Override
    public boolean check(Worker worker) {
        if (StringUtils.isBlank(worker.getEmail())) {
            log.error("Worker validation failed: Email is blank.");
            return false;
        } else if (!EMAIL_PATTERN.matcher(worker.getEmail()).matches()) {
            log.error("Worker validation failed: Invalid email format - " + worker.getEmail());
            return false;
        }
        return checkNext(worker);
    }
}
