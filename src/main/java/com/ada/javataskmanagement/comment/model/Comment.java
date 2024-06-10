package com.ada.javataskmanagement.comment.model;

import com.ada.javataskmanagement.task.model.Task;
import com.ada.javataskmanagement.worker.model.Worker;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String content;

    @ManyToOne
    private Worker author;

    @ManyToOne
    private Task task;
}
