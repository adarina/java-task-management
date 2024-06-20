package com.ada.javataskmanagement.comment.service;

import com.ada.javataskmanagement.comment.dto.CommentDTO;
import com.ada.javataskmanagement.comment.model.Comment;
import com.ada.javataskmanagement.comment.repository.CommentRepository;
import com.ada.javataskmanagement.task.model.Task;
import com.ada.javataskmanagement.task.repository.TaskRepository;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.worker.service.WorkerService;
import com.ada.javataskmanagement.workerproject.repository.WorkerProjectRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final WorkerService workerService;
    private final WorkerProjectRepository workerProjectRepository;
    private final Clock clock;

    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository, WorkerService workerService, WorkerProjectRepository workerProjectRepository, Clock clock) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.workerService = workerService;
        this.workerProjectRepository = workerProjectRepository;
        this.clock = clock;
    }

    public Comment addComment(UUID taskId, CommentDTO commentDTO) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Worker worker = workerService.findWorkerById(commentDTO.getAuthorId());

        boolean isWorkerInProject = workerProjectRepository.existsByWorkerUuidAndProjectUuid(commentDTO.getAuthorId(), task.getProject().getUuid());
        if (!isWorkerInProject) {
            throw new IllegalArgumentException("Worker does not belong to this project");
        }

        Comment newComment = convertToEntity(commentDTO);
        newComment.setCreatedAt(LocalDateTime.now(clock));
        newComment.setTask(task);
        newComment.setAuthor(worker);

        return commentRepository.save(newComment);
    }

    public CommentDTO convertToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setUuid(comment.getUuid());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setModifiedAt(comment.getModifiedAt());
        commentDTO.setContent(comment.getContent());
        commentDTO.setAuthorId(comment.getAuthor().getUuid());
        commentDTO.setTaskId(comment.getTask().getUuid());
        return commentDTO;
    }

    public Comment convertToEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setUuid(commentDTO.getUuid());
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(commentDTO.getCreatedAt());
        comment.setModifiedAt(commentDTO.getModifiedAt());
        return comment;
    }
}