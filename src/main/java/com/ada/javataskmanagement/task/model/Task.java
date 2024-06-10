package com.ada.javataskmanagement.task.model;

import com.ada.javataskmanagement.comment.model.Comment;
import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.worker.model.Worker;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private String title;
    private String shortDescription;
    private String longDescription;
    private LocalDate deadline;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments = Collections.emptyList();

    @ManyToOne
    private Worker createdBy;

    @ManyToOne
    private Worker assignTo;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    private Project project;
}
