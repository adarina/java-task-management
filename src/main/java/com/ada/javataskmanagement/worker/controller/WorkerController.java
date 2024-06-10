package com.ada.javataskmanagement.worker.controller;

import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.worker.repository.WorkerRepository;
import com.ada.javataskmanagement.worker.service.WorkerService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/workers")
public class WorkerController {


    private WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @PostMapping
    public Worker addWorker(@RequestBody Worker worker) {
        return workerService.addWorker(worker);
    }

    @GetMapping("/{id}")
    public Worker getWorker(@PathVariable UUID id) {
        return workerService.findWorkerById(id);
    }

    @GetMapping
    public List<Worker> getAllWorkers() {
        return workerService.getAllWorkers();
    }
}
