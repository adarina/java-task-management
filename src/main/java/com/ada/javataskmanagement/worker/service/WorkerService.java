package com.ada.javataskmanagement.worker.service;

import com.ada.javataskmanagement.worker.dto.WorkerDTO;
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
