package com.ada.javataskmanagement.worker.model;

import com.ada.javataskmanagement.workerproject.model.WorkerProject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private String firstname;

    private String lastname;

    private String email;

    @OneToMany(mappedBy = "worker")
    @JsonIgnore
    private List<WorkerProject> workerProjects = new ArrayList<>();
}
