package com.ada.javataskmanagement.project.service;

import com.ada.javataskmanagement.project.dto.ProjectDTO;
import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.project.repository.ProjectRepository;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.worker.service.WorkerService;
import com.ada.javataskmanagement.workerproject.repository.WorkerProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    ProjectRepository projectRepository;

    @Mock
    WorkerService workerService;

    @Mock
    WorkerProjectRepository workerProjectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void shouldThrowExceptionWhenProjectNotFound() {
        UUID projectId = UUID.randomUUID();
        UUID workerId = UUID.randomUUID();

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> projectService.addWorkerToProject(projectId, workerId));

        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenWorkerNotFound() {
        UUID projectId = UUID.randomUUID();
        UUID workerId = UUID.randomUUID();
        Project project = new Project();
        project.setUuid(projectId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(workerService.findWorkerById(workerId)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> projectService.addWorkerToProject(projectId, workerId));

        assertEquals("Worker not found", exception.getMessage());
    }

    @Test
    void shouldAddWorkerToProject() {
        UUID projectId = UUID.randomUUID();
        UUID workerId = UUID.randomUUID();
        Project project = new Project();
        project.setUuid(projectId);
        project.setName("Marek");
        Worker worker = new Worker();
        worker.setUuid(workerId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(workerService.findWorkerById(workerId)).thenReturn(worker);
        when(workerProjectRepository.existsByWorkerUuidAndProjectUuid(workerId, projectId)).thenReturn(false);

        Project result = projectService.addWorkerToProject(projectId, workerId);

        assertEquals(projectId, result.getUuid());
    }

    @Test
    void shouldNotAddWorkerToProjectWhenAlreadyAssigned() {
        UUID projectId = UUID.randomUUID();
        UUID workerId = UUID.randomUUID();
        Project project = new Project();
        project.setUuid(projectId);
        Worker worker = new Worker();
        worker.setUuid(workerId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(workerService.findWorkerById(workerId)).thenReturn(worker);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> projectService.addWorkerToProject(projectId, workerId));

        assertEquals("Project validation failed.", exception.getMessage());

    }

    @Test
    void shouldAddProject() {
        Project project = new Project();
        project.setName("Test Project");

        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.addProject(project);

        assertEquals("Test Project", result.getName());
    }

    @Test
    void shouldFindProjectById() {
        UUID projectId = UUID.randomUUID();
        Project project = new Project();
        project.setUuid(projectId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        Project result = projectService.findProjectById(projectId);

        assertEquals(projectId, result.getUuid());
    }

    @Test
    void shouldReturnNullWhenProjectNotFoundById() {
        UUID projectId = UUID.randomUUID();

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        Project result = projectService.findProjectById(projectId);

        assertNull(result);
    }

    @Test
    void shouldGetAllProjects() {
        List<Project> projects = new ArrayList<>();
        Project project1 = new Project();
        project1.setName("Project 1");
        projects.add(project1);

        Project project2 = new Project();
        project2.setName("Project 2");
        projects.add(project2);

        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> allProjects = projectService.getAllProjects();

        assertNotNull(allProjects);
        assertEquals(2, allProjects.size());
        assertEquals("Project 1", allProjects.get(0).getName());
        assertEquals("Project 2", allProjects.get(1).getName());
    }

    @Test
    void shouldConvertToDTO() {
        Project project = new Project();
        project.setUuid(UUID.randomUUID());
        project.setName("Test Project");
        project.setDescription("Test Description");

        ProjectDTO projectDTO = projectService.convertToDTO(project);

        assertEquals(project.getUuid(), projectDTO.getUuid());
        assertEquals("Test Project", projectDTO.getName());
        assertEquals("Test Description", projectDTO.getDescription());
    }

    @Test
    void shouldConvertToEntity() {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Test Project");
        projectDTO.setDescription("Test Description");

        Project project = projectService.convertToEntity(projectDTO);

        assertEquals("Test Project", project.getName());
        assertEquals("Test Description", project.getDescription());
    }
}
