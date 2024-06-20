package com.ada.javataskmanagement.task.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class TaskDTO {
    private UUID id;
    private String title;
    private String shortDescription;
    private String longDescription;
    private LocalDate deadline;
    private UUID createdById;
    private UUID assignToId;
    private UUID projectId;
    private String status;
}
