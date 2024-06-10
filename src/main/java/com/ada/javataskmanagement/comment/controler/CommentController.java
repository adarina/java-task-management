package com.ada.javataskmanagement.comment.controler;

import com.ada.javataskmanagement.comment.model.Comment;
import com.ada.javataskmanagement.comment.service.CommentService;
import com.ada.javataskmanagement.task.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable UUID taskId, @RequestBody Comment comment) {
        Comment newComment = commentService.addComment(taskId, comment);
        return ResponseEntity.ok(newComment);
    }
}
