package com.ada.javataskmanagement.comment.service;

import com.ada.javataskmanagement.comment.dto.CommentDTO;
import com.ada.javataskmanagement.comment.model.Comment;
import com.ada.javataskmanagement.comment.repository.CommentRepository;
import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.project.repository.ProjectRepository;
import com.ada.javataskmanagement.task.model.Task;
import com.ada.javataskmanagement.task.repository.TaskRepository;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.worker.service.WorkerService;
import com.ada.javataskmanagement.workerproject.model.WorkerProject;
import com.ada.javataskmanagement.workerproject.repository.WorkerProjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@SpringBootTest
@Transactional
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private WorkerProjectRepository workerProjectRepository;

    @Autowired
    private Clock clock;

    private Worker defaultWorker;
    private Task defaultTask;

    @BeforeEach
    public void setUp() {
        defaultWorker = new Worker();
        defaultWorker.setFirstname("Marek");
        defaultWorker.setLastname("Mostowiak");
        defaultWorker.setEmail("mm@sth.com");
        defaultWorker = workerService.addWorker(defaultWorker);

        Project defaultProject = new Project();
        defaultProject.setName("Default Project");
        defaultProject.setDescription("Description");
        defaultProject = projectRepository.save(defaultProject);

        workerProjectRepository.save(new WorkerProject(defaultWorker, defaultProject));

        defaultTask = new Task();
        defaultTask.setTitle("Default Task");
        defaultTask.setShortDescription("Short description");
        defaultTask.setLongDescription("Long description");
        defaultTask.setDeadline(LocalDate.now(Clock.system(ZoneId.of("UTC"))).plusDays(5));
        defaultTask.setCreatedBy(defaultWorker);
        defaultTask.setProject(defaultProject);
        defaultTask = taskRepository.save(defaultTask);
    }

    @Test
    public void should_AddComment_When_ValidCommentDTO() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("Test comment");
        commentDTO.setAuthorId(defaultWorker.getUuid());
        commentDTO.setTaskId(defaultTask.getUuid());

        Comment newComment = commentService.addComment(defaultTask.getUuid(), commentDTO);
        Comment savedComment = commentRepository.findById(newComment.getUuid()).orElse(null);

        Assertions.assertNotNull(savedComment);
        Assertions.assertEquals("Test comment", savedComment.getContent());
        Assertions.assertEquals(defaultWorker.getUuid(), savedComment.getAuthor().getUuid());
        Assertions.assertEquals(defaultTask.getUuid(), savedComment.getTask().getUuid());
    }

    @Test
    public void should_ThrowException_When_WorkerNotInProject() {
        Worker anotherWorker = new Worker();
        anotherWorker.setFirstname("Hanka");
        anotherWorker.setLastname("Mostowiak");
        anotherWorker.setEmail("h.mostowiak@sth.com");
        Worker savedAnotherWorker = workerService.addWorker(anotherWorker);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("Test comment from another worker");
        commentDTO.setAuthorId(savedAnotherWorker.getUuid());
        commentDTO.setTaskId(defaultTask.getUuid());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            commentService.addComment(defaultTask.getUuid(), commentDTO);
        });
    }

    @Test
    public void should_ConvertToDTO_When_ValidComment() {
        Comment comment = new Comment();
        comment.setContent("Test comment");
        comment.setCreatedAt(LocalDateTime.now(clock));
        comment.setModifiedAt(LocalDateTime.now(clock));
        comment.setAuthor(defaultWorker);
        comment.setTask(defaultTask);
        comment = commentRepository.save(comment);

        CommentDTO commentDTO = commentService.convertToDTO(comment);

        Assertions.assertEquals(comment.getUuid(), commentDTO.getUuid());
        Assertions.assertEquals(comment.getContent(), commentDTO.getContent());
        Assertions.assertEquals(comment.getAuthor().getUuid(), commentDTO.getAuthorId());
        Assertions.assertEquals(comment.getTask().getUuid(), commentDTO.getTaskId());
    }

    @Test
    public void should_ConvertToEntity_When_ValidCommentDTO() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setUuid(UUID.randomUUID());
        commentDTO.setContent("Test comment");
        commentDTO.setCreatedAt(LocalDateTime.now(clock));
        commentDTO.setModifiedAt(LocalDateTime.now(clock));

        Comment comment = commentService.convertToEntity(commentDTO);

        Assertions.assertEquals(commentDTO.getUuid(), comment.getUuid());
        Assertions.assertEquals(commentDTO.getContent(), comment.getContent());
        Assertions.assertEquals(commentDTO.getCreatedAt(), comment.getCreatedAt());
        Assertions.assertEquals(commentDTO.getModifiedAt(), comment.getModifiedAt());
    }
}
