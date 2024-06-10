package com.ada.javataskmanagement.workerproject.repository;

import com.ada.javataskmanagement.workerproject.model.WorkerProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkerProjectRepository extends JpaRepository<WorkerProject, UUID> {
    boolean existsByWorkerUuidAndProjectUuid(UUID workerId, UUID projectId);
}
