package com.ada.javataskmanagement.worker.service;

import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.worker.repository.WorkerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WorkerService {

    private final WorkerRepository workerRepository;

    public WorkerService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    public Worker findWorkerById(UUID workerId) {
        return workerRepository.findById(workerId).orElse(null);
    }

    public Worker addWorker(Worker worker) {
        return workerRepository.save(worker);
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }
}
