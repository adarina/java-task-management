package com.ada.javataskmanagement.comment.service;

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
import java.util.List;
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

    public Comment addComment(UUID taskId, Comment comment) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Worker worker = workerService.findWorkerById(comment.getUuid());

        boolean isWorkerInProject = workerProjectRepository.existsByWorkerUuidAndProjectUuid(comment.getUuid(), task.getProject().getUuid());
        if (!isWorkerInProject) {
            throw new IllegalArgumentException("Worker does not belong to this project");
        }

        Comment newComment = new Comment();
        newComment.setCreatedAt(LocalDateTime.now(clock));
        newComment.setContent(comment.getContent());
        newComment.setAuthor(worker);
        newComment.setTask(task);

        return commentRepository.save(newComment);
    }
}
