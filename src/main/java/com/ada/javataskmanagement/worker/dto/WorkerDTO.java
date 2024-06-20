package com.ada.javataskmanagement.worker.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class WorkerDTO {
    private UUID uuid;
    private String firstname;
    private String lastname;
    private String email;
}
