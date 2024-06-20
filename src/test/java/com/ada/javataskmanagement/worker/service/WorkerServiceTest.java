package com.ada.javataskmanagement.worker.service;

import com.ada.javataskmanagement.worker.dto.WorkerDTO;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.worker.repository.WorkerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
public class WorkerServiceTest {

    @Autowired
    private WorkerService workerService;

    @Autowired
    private WorkerRepository workerRepository;

    @Test
    public void should_AddWorker_When_ValidWorker() {

        Worker worker = new Worker();
        worker.setFirstname("Marek");
        worker.setLastname("Mostowiak");
        worker.setEmail("m.mostowiak@sth.com");

        Worker savedWorker = workerService.addWorker(worker);

        Assertions.assertNotNull(savedWorker.getUuid());
        Assertions.assertEquals("Marek", savedWorker.getFirstname());
        Assertions.assertEquals("Mostowiak", savedWorker.getLastname());
        Assertions.assertEquals("m.mostowiak@sth.com", savedWorker.getEmail());
    }

    @Test
    public void should_FindWorkerById_When_WorkerExists() {

        Worker worker = new Worker();
        worker.setFirstname("Marek");
        worker.setLastname("Mostowiak");
        worker.setEmail("m.mostowiak@sth.com");
        Worker savedWorker = workerRepository.save(worker);

        Worker foundWorker = workerService.findWorkerById(savedWorker.getUuid());

        Assertions.assertNotNull(foundWorker);
        Assertions.assertEquals("Marek", foundWorker.getFirstname());
        Assertions.assertEquals("Mostowiak", foundWorker.getLastname());
        Assertions.assertEquals("m.mostowiak@sth.com", foundWorker.getEmail());
    }

    @Test
    public void should_GetAllWorkers_When_WorkersExist() {

        Worker worker1 = new Worker();
        worker1.setFirstname("Marek");
        worker1.setLastname("Mostowiak");
        worker1.setEmail("m.mostowiak@sth.com");
        workerRepository.save(worker1);

        Worker worker2 = new Worker();
        worker2.setFirstname("Hanka");
        worker2.setLastname("Mostowiak");
        worker2.setEmail("h.mostowiak@sth.com");
        workerRepository.save(worker2);

        List<Worker> allWorkers = workerService.getAllWorkers();

        Assertions.assertEquals(2, allWorkers.size());
        Assertions.assertTrue(allWorkers.stream().anyMatch(worker -> "Marek".equals(worker.getFirstname()) && "Mostowiak".equals(worker.getLastname())));
        Assertions.assertTrue(allWorkers.stream().anyMatch(worker -> "Hanka".equals(worker.getFirstname()) && "Mostowiak".equals(worker.getLastname())));
    }

    @Test
    public void should_ConvertToDTO_When_ValidWorker() {

        Worker worker = new Worker();
        worker.setUuid(UUID.randomUUID());
        worker.setFirstname("Marek");
        worker.setLastname("Mostowiak");
        worker.setEmail("m.mostowiak@sth.com");


        WorkerDTO workerDTO = workerService.convertToDTO(worker);


        Assertions.assertEquals(worker.getUuid(), workerDTO.getUuid());
        Assertions.assertEquals("Marek", workerDTO.getFirstname());
        Assertions.assertEquals("Mostowiak", workerDTO.getLastname());
        Assertions.assertEquals("m.mostowiak@sth.com", workerDTO.getEmail());
    }

    @Test
    public void should_ConvertToEntity_When_ValidWorkerDTO() {

        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setFirstname("Marek");
        workerDTO.setLastname("Mostowiak");
        workerDTO.setEmail("m.mostowiak@sth.com");

        Worker worker = workerService.convertToEntity(workerDTO);

        Assertions.assertEquals("Marek", worker.getFirstname());
        Assertions.assertEquals("Mostowiak", worker.getLastname());
        Assertions.assertEquals("m.mostowiak@sth.com", worker.getEmail());
    }
}
