package com.ada.javataskmanagement.task.validation;

import com.ada.javataskmanagement.task.model.Task;
import com.ada.javataskmanagement.workerproject.repository.WorkerProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerInProjectTaskValidator extends TaskValidator {

    private static final Logger logger = LoggerFactory.getLogger(ProjectTaskValidator.class);

    private final WorkerProjectRepository workerProjectRepository;

    public WorkerInProjectTaskValidator(WorkerProjectRepository workerProjectRepository) {
        this.workerProjectRepository = workerProjectRepository;
    }

    @Override
    public boolean check(Task task) {
        boolean exists = workerProjectRepository.existsByWorkerUuidAndProjectUuid(task.getCreatedBy().getUuid(), task.getProject().getUuid());
        if (!exists) {
            logger.error("Worker {} is not assigned to Project {}", task.getCreatedBy().getUuid(), task.getProject().getUuid());
            return false;
        }
        return checkNext(task);
    }

}
