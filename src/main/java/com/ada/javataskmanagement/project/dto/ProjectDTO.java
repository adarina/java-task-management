package com.ada.javataskmanagement.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProjectDTO {
    private UUID uuid;
    private String name;
    private String description;
}
