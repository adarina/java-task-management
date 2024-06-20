package com.ada.javataskmanagement.comment.controler;

import com.ada.javataskmanagement.comment.dto.CommentDTO;
import com.ada.javataskmanagement.comment.model.Comment;
import com.ada.javataskmanagement.comment.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@PathVariable UUID taskId, @RequestBody CommentDTO commentDTO) {
        Comment newComment = commentService.addComment(taskId, commentDTO);
        CommentDTO responseDTO = commentService.convertToDTO(newComment);
        return ResponseEntity.ok(responseDTO);
    }
}
