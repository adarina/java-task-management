package com.ada.javataskmanagement.worker.service;

import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.worker.repository.WorkerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WorkerServiceTest {

    @Mock
    private WorkerRepository workerRepository;
    @InjectMocks
    WorkerService workerService;

    @Test
    void shouldThrowExceptionWhenValidationWorkerHasFail() {
        Worker worker = new Worker();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> workerService.addWorker(worker));

        assertEquals("Worker validation failed.", exception.getMessage());
    }

    @Test
    void shouldFindWorkerById() {
        UUID workerId = UUID.randomUUID();
        Worker worker = new Worker();
        worker.setUuid(workerId);

        when(workerRepository.findById(workerId)).thenReturn(Optional.of(worker));

        Worker foundWorker = workerService.findWorkerById(workerId);

        assertEquals(workerId, foundWorker.getUuid());
    }

    @Test
    void shouldReturnNullWhenWorkerNotFoundById() {
        UUID workerId = UUID.randomUUID();

        when(workerRepository.findById(workerId)).thenReturn(Optional.empty());

        Worker foundWorker = workerService.findWorkerById(workerId);

        assertNull(foundWorker);
    }

    @Test
    void shouldGetAllWorkers() {
        List<Worker> workers = new ArrayList<>();
        Worker worker1 = new Worker();
        worker1.setFirstname("Marek");
        worker1.setLastname("Mostowiak");
        worker1.setEmail("mm@m.com");
        workers.add(worker1);

        Worker worker2 = new Worker();
        worker2.setFirstname("Hanka");
        worker2.setLastname("Mostowiak");
        worker2.setEmail("hm@m.com");
        workers.add(worker2);

        when(workerRepository.findAll()).thenReturn(workers);

        List<Worker> allWorkers = workerService.getAllWorkers();

        assertEquals(2, allWorkers.size());
    }

    @Test
    void shouldThrowExceptionWhenWorkerIdIsNull() {
        UUID workerId = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> workerService.findWorkerById(workerId));

        assertEquals("Worker ID cannot be null.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddingNullWorker() {
        Worker worker = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> workerService.addWorker(worker));

        assertEquals("Worker cannot be null.", exception.getMessage());
    }

    @Test
    void shouldReturnEmptyListWhenNoWorkersFound() {
        when(workerRepository.findAll()).thenReturn(new ArrayList<>());

        List<Worker> allWorkers = workerService.getAllWorkers();

        assertTrue(allWorkers.isEmpty());
    }
}