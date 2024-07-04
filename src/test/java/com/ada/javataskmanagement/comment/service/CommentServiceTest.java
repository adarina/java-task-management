package com.ada.javataskmanagement.comment.service;

import com.ada.javataskmanagement.comment.dto.CommentDTO;
import com.ada.javataskmanagement.comment.repository.CommentRepository;
import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.task.model.Task;
import com.ada.javataskmanagement.task.repository.TaskRepository;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.worker.service.WorkerService;
import com.ada.javataskmanagement.workerproject.repository.WorkerProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private WorkerService workerService;

    @Mock
    private WorkerProjectRepository workerProjectRepository;

    @Mock
    private Clock clock;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(
                Instant.parse("2024-07-01T10:00:00Z"),
                ZoneId.of("UTC")
        );
        commentService = new CommentService(commentRepository, taskRepository, workerService, workerProjectRepository, clock);
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFoundForAddComment() {
        UUID taskId = UUID.randomUUID();
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setAuthorId(UUID.randomUUID());
        commentDTO.setContent("Test comment content.");

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> commentService.addComment(taskId, commentDTO));

        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenWorkerNotInProjectForAddComment() {
        UUID taskId = UUID.randomUUID();
        CommentDTO commentDTO = new CommentDTO();
        UUID authorId = UUID.randomUUID();
        commentDTO.setAuthorId(authorId);
        commentDTO.setContent("Test comment.");

        Task task = new Task();
        task.setUuid(taskId);

        Project project = new Project();
        project.setUuid(UUID.randomUUID());

        task.setProject(project);
        Worker worker = new Worker();
        worker.setUuid(authorId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(workerService.findWorkerById(authorId)).thenReturn(worker);
        when(workerProjectRepository.existsByWorkerUuidAndProjectUuid(authorId, task.getProject().getUuid())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> commentService.addComment(taskId, commentDTO));

        assertEquals("Worker does not belong to this project", exception.getMessage());
    }

    //    @Test
//    void shouldAddCommentSuccessfully() {
//        UUID taskId = UUID.randomUUID();
//        UUID authorId = UUID.randomUUID();
//        String content = "This is a test comment.";
//
//        Task task = new Task();
//        task.setUuid(taskId);
//
//        Project project = new Project();
//        project.setUuid(UUID.randomUUID());
//
//        task.setProject(project);
//
//        Worker worker = new Worker();
//        worker.setUuid(authorId);
//
//        CommentDTO commentDTO = new CommentDTO();
//        commentDTO.setAuthorId(authorId);
//        commentDTO.setContent(content);
//
//        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
//        when(workerService.findWorkerById(authorId)).thenReturn(worker);
//        when(workerProjectRepository.existsByWorkerUuidAndProjectUuid(authorId, task.getProject().getUuid())).thenReturn(true);
//
//        LocalDateTime currentDateTime = LocalDateTime.now(clock);
//        // TODO
//
//        Comment savedComment = new Comment();
//        savedComment.setUuid(UUID.randomUUID());
//        savedComment.setCreatedAt(currentDateTime);
//        savedComment.setModifiedAt(null);
//
//        when(commentRepository.save(savedComment)).thenReturn(savedComment);
//
//        Comment addedComment = commentService.addComment(taskId, commentDTO);
//
//        assertNotNull(addedComment);
//        assertEquals(content, addedComment.getContent());
//        assertEquals(taskId, addedComment.getTask().getUuid());
//        assertEquals(authorId, addedComment.getAuthor().getUuid());
//        assertEquals(currentDateTime, addedComment.getCreatedAt());
//    }
}
