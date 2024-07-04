package com.ada.javataskmanagement.worker.service;

import com.ada.javataskmanagement.worker.validation.EmailWorkerValidator;
import com.ada.javataskmanagement.worker.validation.FirstnameWorkerValidator;
import com.ada.javataskmanagement.worker.validation.LastnameWorkerValidator;
import com.ada.javataskmanagement.worker.validation.WorkerValidator;
import com.ada.javataskmanagement.worker.dto.WorkerDTO;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.worker.repository.WorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WorkerService {

    private static final Logger logger = LoggerFactory.getLogger(WorkerService.class);

    private final WorkerRepository workerRepository;

    private final WorkerValidator workerValidator;

    public WorkerService(WorkerRepository workerRepository) {

        this.workerRepository = workerRepository;
        workerValidator = WorkerValidator.link(
                new EmailWorkerValidator(),
                new FirstnameWorkerValidator(),
                new LastnameWorkerValidator()
        );
    }

    public Worker findWorkerById(UUID workerId) {
        if (workerId == null) {
            throw new IllegalArgumentException("Worker ID cannot be null.");
        }
        return workerRepository.findById(workerId).orElse(null);
    }

    public Worker addWorker(Worker worker) {
        if (worker == null) {
            throw new IllegalArgumentException("Worker cannot be null.");
        }
        if (!validateWorker(worker)) {
            throw new IllegalArgumentException("Worker validation failed.");
        }
        return workerRepository.save(worker);
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public boolean validateWorker(Worker worker) {
        boolean isValid = workerValidator.check(worker);
        if (!isValid) {
            logger.error("Worker validation failed: {}", worker);
        }
        return isValid;
    }

    public WorkerDTO convertToDTO(Worker worker) {
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setUuid(worker.getUuid());
        workerDTO.setFirstname(worker.getFirstname());
        workerDTO.setLastname(worker.getLastname());
        workerDTO.setEmail(worker.getEmail());
        return workerDTO;
    }

    public Worker convertToEntity(WorkerDTO workerDTO) {
        Worker worker = new Worker();
        worker.setFirstname(workerDTO.getFirstname());
        worker.setLastname(workerDTO.getLastname());
        worker.setEmail(workerDTO.getEmail());
        return worker;
    }
}
