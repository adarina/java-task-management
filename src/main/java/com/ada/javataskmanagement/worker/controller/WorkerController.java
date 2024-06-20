package com.ada.javataskmanagement.worker.controller;

import com.ada.javataskmanagement.worker.dto.WorkerDTO;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.worker.service.WorkerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/v1/workers")
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @PostMapping
    public WorkerDTO addWorker(@RequestBody WorkerDTO workerDTO) {
        Worker worker = workerService.convertToEntity(workerDTO);
        Worker savedWorker = workerService.addWorker(worker);
        return workerService.convertToDTO(savedWorker);
    }

    @GetMapping("/{id}")
    public WorkerDTO getWorker(@PathVariable UUID id) {
        Worker worker = workerService.findWorkerById(id);
        return worker != null ? workerService.convertToDTO(worker) : null;
    }

    @GetMapping
    public List<WorkerDTO> getAllWorkers() {
        return workerService.getAllWorkers().stream()
                .map(workerService::convertToDTO)
                .collect(Collectors.toList());
    }
}
