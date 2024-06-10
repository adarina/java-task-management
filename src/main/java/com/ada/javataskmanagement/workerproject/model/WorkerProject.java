package com.ada.javataskmanagement.workerproject.model;

import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.worker.model.Worker;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "worker_project")
public class WorkerProject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public WorkerProject() {
    }

    public WorkerProject(Worker worker, Project project) {
        this.worker = worker;
        this.project = project;
    }

}
