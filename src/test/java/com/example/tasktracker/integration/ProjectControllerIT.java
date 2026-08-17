package com.example.tasktracker.integration;

import com.example.tasktracker.entity.*;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.TaskRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ProjectControllerIT {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer postgreSQLContainer =
            new PostgreSQLContainer(
                    DockerImageName.parse("postgres:16")
            );

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void cleanDatabase() {
        projectRepository.deleteAll();
    }

    @Test
    void shouldCreateProject() throws Exception {
        String requestBody = """
                {
                    "title": "home",
                    "description": "home tasks",
                    "status": "ACTIVE"
                }
                """;

        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("home"))
                .andExpect(jsonPath("$.description").value("home tasks"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        assertThat(projectRepository.existsProjectByTitle("home")).isTrue();
    }

    @Test
    void shouldReturnConflictWhenProjectExists() throws Exception {
        projectRepository.saveAndFlush(
                new Project("home", "home tasks", ProjectStatus.ACTIVE)
        );

        String requestBody = """
                {
                    "title": "home",
                    "description": "home tasks",
                    "status": "ACTIVE"
                }
                """;

        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldGetAllProjects() throws Exception {
        projectRepository.saveAll(List.of(
                new Project("home", "home tasks", ProjectStatus.ACTIVE),
                new Project("work", "work tasks", ProjectStatus.ACTIVE),
                new Project("gym", "exercises", ProjectStatus.ACTIVE)
        ));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].title").value("home"))
                .andExpect(jsonPath("$[0].description").value("home tasks"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[1].title").value("work"))
                .andExpect(jsonPath("$[1].description").value("work tasks"))
                .andExpect(jsonPath("$[1].status").value("ACTIVE"))
                .andExpect(jsonPath("$[2].title").value("gym"))
                .andExpect(jsonPath("$[2].description").value("exercises"))
                .andExpect(jsonPath("$[2].status").value("ACTIVE"));
    }

    @Test
    void shouldGetEmptyListOfProjects() throws Exception {
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldGetProjectById() throws Exception {
        Project project = projectRepository.save(
                new Project("home", "home tasks", ProjectStatus.ACTIVE)
        );

        mockMvc.perform(get("/api/projects/{id}", project.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(project.getId()))
                .andExpect(jsonPath("$.title").value("home"))
                .andExpect(jsonPath("$.description").value("home tasks"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturnNotFoundWhenGetAndProjectDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateProjectInfo() throws Exception {
        Project project = projectRepository.save(
                new Project("hme", "home", ProjectStatus.ACTIVE)
        );

        String requestBody = """
                {
                    "title": "home",
                    "description": "home tasks"
                }
                """;

        mockMvc.perform(put("/api/projects/{id}", project.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(project.getId()))
                .andExpect(jsonPath("$.title").value("home"))
                .andExpect(jsonPath("$.description").value("home tasks"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturnNotFoundWhenPutAndProjectDoesNotExist() throws Exception {
        String requestBody = """
                {
                    "title": "home",
                    "description": "home tasks"
                }
                """;

        mockMvc.perform(put("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteProjectById() throws Exception {
        Project project = projectRepository.save(
                new Project("home", "home tasks", ProjectStatus.ACTIVE)
        );

        mockMvc.perform(delete("/api/projects/{id}", project.getId()))
                .andExpect(status().isNoContent());

        assertThat(projectRepository.existsProjectByTitle("home")).isFalse();
    }

    @Test
    void shouldReturnNotFoundWhenDeleteAndProjectDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldArchiveProjectById() throws Exception {
        Project project = projectRepository.save(
                new Project("home", "home tasks", ProjectStatus.ACTIVE)
        );

        String requestBody = """
                {
                    "status": "ARCHIVE"
                }
                """;

        mockMvc.perform(patch("/api/projects/{id}/archive", project.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(project.getId()))
                .andExpect(jsonPath("$.title").value("home"))
                .andExpect(jsonPath("$.description").value("home tasks"))
                .andExpect(jsonPath("$.status").value("ARCHIVE"));
    }

    @Test
    void shouldReturnNotFoundWhenArchiveAndProjectDoesNotExist() throws Exception {
        String requestBody = """
                {
                    "status": "ARCHIVE"
                }
                """;

        mockMvc.perform(patch("/api/projects/1/archive")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateTask() throws Exception {
        Project project = projectRepository.saveAndFlush(
                new Project("home", "home tasks", ProjectStatus.ACTIVE)
        );

        String requestBody = """
                {
                    "title": "clean bedroom",
                    "description": "vacuum clean the floor and wash the desk",
                    "status": "TODO",
                    "priority": "MEDIUM"
                }
                """;

        MvcResult result = mockMvc.perform(post("/api/projects/{projectId}/tasks", project.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("clean bedroom"))
                .andExpect(jsonPath("$.description").value("vacuum clean the floor and wash the desk"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andReturn();

        Number taskIdNumber = JsonPath.read(
                result.getResponse().getContentAsString(),
                "$.id"
        );

        Long taskId = taskIdNumber.longValue();

        assertThat(taskRepository.findById(taskId)).isPresent();
    }

    @Test
    void shouldReturnNotFoundWhenCreateTaskAndProjectDoesNotExist() throws Exception {
        String requestBody = """
                {
                    "title": "clean bedroom",
                    "description": "vacuum clean the floor and wash the desk",
                    "status": "TODO",
                    "priority": "MEDIUM"
                }
                """;

        mockMvc.perform(post("/api/projects/1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetTasksByProjectId() throws Exception {
        Project project = projectRepository.saveAndFlush(
                new Project("home", "home tasks", ProjectStatus.ACTIVE)
        );

        taskRepository.saveAll(List.of(
                new Task(
                        "clean the room",
                        "",
                        TaskStatus.TODO,
                        TaskPriority.MEDIUM,
                        project
                ),
                new Task(
                        "take out the garbage",
                        "from the kitchen and my room",
                        TaskStatus.TODO,
                        TaskPriority.LOW,
                        project
                ),
                new Task(
                        "feed the cat",
                        "",
                        TaskStatus.TODO,
                        TaskPriority.HIGH,
                        project
                )
        ));

        mockMvc.perform(get("/api/projects/{projectId}/tasks", project.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].title").value("clean the room"))
                .andExpect(jsonPath("$[0].description").value(""))
                .andExpect(jsonPath("$[0].status").value("TODO"))
                .andExpect(jsonPath("$[0].priority").value("MEDIUM"))
                .andExpect(jsonPath("$[1].title").value("take out the garbage"))
                .andExpect(jsonPath("$[1].description").value("from the kitchen and my room"))
                .andExpect(jsonPath("$[1].status").value("TODO"))
                .andExpect(jsonPath("$[1].priority").value("LOW"))
                .andExpect(jsonPath("$[2].title").value("feed the cat"))
                .andExpect(jsonPath("$[2].description").value(""))
                .andExpect(jsonPath("$[2].status").value("TODO"))
                .andExpect(jsonPath("$[2].priority").value("HIGH"));
    }

    @Test
    void shouldGetEmptyListOfTasks() throws Exception {
        Project project = projectRepository.saveAndFlush(
                new Project("home", "home tasks", ProjectStatus.ACTIVE)
        );

        mockMvc.perform(get("/api/projects/{projectId}/tasks", project.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
