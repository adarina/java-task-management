package com.ada.javataskmanagement.project.validation;

import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.workerproject.repository.WorkerProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class AssignedWorkerValidator extends ProjectValidator {
    private final WorkerProjectRepository workerProjectRepository;

    public AssignedWorkerValidator(WorkerProjectRepository workerProjectRepository) {
        this.workerProjectRepository = workerProjectRepository;
    }

    @Override
    public boolean check(Project project, Worker worker) {
        boolean alreadyAssigned = workerProjectRepository.existsByWorkerUuidAndProjectUuid(worker.getUuid(), project.getUuid());
        if (alreadyAssigned) {
            log.error("Worker is already assigned to the project.");
            throw new IllegalArgumentException("Worker is already assigned to the project.");
        }
        return checkNext(project, worker);
    }
}

