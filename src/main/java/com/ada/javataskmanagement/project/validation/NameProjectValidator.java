package com.ada.javataskmanagement.project.validation;

import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.task.validation.DeadlineTaskValidator;
import com.ada.javataskmanagement.worker.model.Worker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class NameProjectValidator extends ProjectValidator {

    private static final Pattern NAME_PATTERN = Pattern.compile("[A-Za-z]+");

    private static final Logger logger = LoggerFactory.getLogger(DeadlineTaskValidator.class);

    @Override
    public boolean check(Project project, Worker worker) {
        if (StringUtils.isBlank(project.getName())) {
            logger.error("Project validation failed: Name is blank.");
            return false;
        } else if (!NAME_PATTERN.matcher(project.getName()).matches()) {
            logger.error("Project validation failed: Invalid name format - " + project.getName());
            return false;
        }
        return checkNext(project, worker);
    }
}
