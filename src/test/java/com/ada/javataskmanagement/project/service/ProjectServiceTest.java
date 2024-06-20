package com.ada.javataskmanagement.project.service;

import com.ada.javataskmanagement.project.dto.ProjectDTO;
import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.project.repository.ProjectRepository;
import com.ada.javataskmanagement.worker.model.Worker;
import com.ada.javataskmanagement.worker.service.WorkerService;
import com.ada.javataskmanagement.workerproject.model.WorkerProject;
import com.ada.javataskmanagement.workerproject.repository.WorkerProjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private WorkerProjectRepository workerProjectRepository;

    @Test
    public void should_AddProject_When_ValidProject() {
        Project project = new Project();
        project.setName("Project");
        project.setDescription("Test");

        Project savedProject = projectService.addProject(project);

        Assertions.assertNotNull(savedProject.getUuid());
        Assertions.assertEquals("Project", savedProject.getName());
        Assertions.assertEquals("Test", savedProject.getDescription());
    }

    @Test
    public void should_FindProjectById_When_ProjectExists() {
        Project project = new Project();
        project.setName("Project");
        project.setDescription("Test");
        Project savedProject = projectRepository.save(project);

        Project foundProject = projectService.findProjectById(savedProject.getUuid());

        Assertions.assertNotNull(foundProject);
        Assertions.assertEquals("Project", foundProject.getName());
        Assertions.assertEquals("Test", foundProject.getDescription());
    }

    @Test
    public void should_GetAllProjects_When_ProjectsExist() {
        Project project1 = new Project();
        project1.setName("Project 1");
        project1.setDescription("First test");
        projectRepository.save(project1);

        Project project2 = new Project();
        project2.setName("Project 2");
        project2.setDescription("Second test");
        projectRepository.save(project2);

        List<Project> allProjects = projectService.getAllProjects();
        Assertions.assertEquals(2, allProjects.size());
        Assertions.assertTrue(allProjects.stream().anyMatch(p -> "Project 1".equals(p.getName()) && "First test".equals(p.getDescription())));
        Assertions.assertTrue(allProjects.stream().anyMatch(p -> "Project 2".equals(p.getName()) && "Second test".equals(p.getDescription())));
    }

    @Test
    public void should_ConvertToDTO_When_ValidProject() {
        Project project = new Project();
        project.setUuid(UUID.randomUUID());
        project.setName("Project");
        project.setDescription("Test");

        ProjectDTO projectDTO = projectService.convertToDTO(project);

        Assertions.assertEquals(project.getUuid(), projectDTO.getUuid());
        Assertions.assertEquals("Project", projectDTO.getName());
        Assertions.assertEquals("Test", projectDTO.getDescription());
    }

    @Test
    public void should_ConvertToEntity_When_ValidProjectDTO() {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Project");
        projectDTO.setDescription("Test");

        Project project = projectService.convertToEntity(projectDTO);

        Assertions.assertEquals("Project", project.getName());
        Assertions.assertEquals("Test", project.getDescription());
    }

    @Test
    public void should_AddWorkerToProject_When_ValidWorkerAndProject() {

        Project project = new Project();
        project.setName("Project with Workers");
        project.setDescription("Project description with workers.");
        Project savedProject = projectRepository.save(project);

        Worker worker = new Worker();
        worker.setFirstname("M");
        worker.setLastname("M");
        worker.setEmail("mm@sth.com");
        Worker savedWorker = workerService.addWorker(worker);

        Project updatedProject = projectService.addWorkerToProject(savedProject.getUuid(), savedWorker.getUuid());

        List<WorkerProject> workerProjects = workerProjectRepository.findAll();
        boolean workerAssigned = workerProjects.stream()
                .anyMatch(wp -> wp.getWorker().getUuid().equals(savedWorker.getUuid()) &&
                        wp.getProject().getUuid().equals(savedProject.getUuid()));

        Assertions.assertTrue(workerAssigned);
    }

}
