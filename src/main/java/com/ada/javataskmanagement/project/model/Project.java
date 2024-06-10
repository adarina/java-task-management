package com.ada.javataskmanagement.project.model;

import com.ada.javataskmanagement.workerproject.model.WorkerProject;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private String name;
    private String description;

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private List<WorkerProject> workerProjects = new ArrayList<>();

}