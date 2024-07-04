package com.ada.javataskmanagement.project.validation;

import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.workerproject.repository.WorkerProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssignedWorkerValidator extends ProjectValidator {

    private static final Logger logger = LoggerFactory.getLogger(AssignedWorkerValidator.class);
    private final WorkerProjectRepository workerProjectRepository;

    public AssignedWorkerValidator(WorkerProjectRepository workerProjectRepository) {
        this.workerProjectRepository = workerProjectRepository;
    }

    @Override
    public boolean check(Project project, Worker worker) {
        boolean alreadyAssigned = workerProjectRepository.existsByWorkerUuidAndProjectUuid(worker.getUuid(), project.getUuid());
        if (alreadyAssigned) {
            logger.error("Worker is already assigned to the project.");
            throw new IllegalArgumentException("Worker is already assigned to the project.");
        }
        return checkNext(project, worker);
    }
}

