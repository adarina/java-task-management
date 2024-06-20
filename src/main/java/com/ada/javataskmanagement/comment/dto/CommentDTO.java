package com.ada.javataskmanagement.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CommentDTO {
    private UUID uuid;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String content;
    private UUID authorId;
    private UUID taskId;
}
