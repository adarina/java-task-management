package com.ada.javataskmanagement.worker.repository;

import com.ada.javataskmanagement.worker.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkerRepository extends JpaRepository<Worker, UUID> {
}
