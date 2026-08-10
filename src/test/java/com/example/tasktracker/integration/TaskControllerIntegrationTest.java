package com.example.tasktracker.integration;

import com.example.tasktracker.entity.*;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class TaskControllerIntegrationTest {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer postgreSQLContainer =
            new PostgreSQLContainer(
                    DockerImageName.parse("postgres:16")
            );

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @BeforeEach
    void cleanDatabase() {
        taskRepository.deleteAll();
    }

    @Test
    void shouldGetTaskById() throws Exception {
        Project project = projectRepository.saveAndFlush(
                new Project("home", "home tasks", ProjectStatus.ACTIVE)
        );

        Task task = taskRepository.save(
                new Task(
                        "feed the cat",
                        "",
                        TaskStatus.TODO,
                        TaskPriority.HIGH,
                        project
                )
        );

        mockMvc.perform(get("/api/tasks/{id}", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value("feed the cat"))
                .andExpect(jsonPath("$.description").value(""))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test
    void shouldReturnNotFoundWhenGetAndTaskDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateTaskInfo() throws Exception {
        Project project = projectRepository.saveAndFlush(
                new Project("home", "home tasks", ProjectStatus.ACTIVE)
        );

        Task task = taskRepository.save(
                new Task(
                        "feed the cat",
                        "",
                        TaskStatus.TODO,
                        TaskPriority.HIGH,
                        project
                )
        );

        String requestBody = """
                {
                    "title": "feed the cat",
                    "description": "grab the feed from the table",
                    "priority": "HIGH"
                }
                """;

        mockMvc.perform(put("/api/tasks/{id}", task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("feed the cat"))
                .andExpect(jsonPath("$.description")
                        .value("grab the feed from the table"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdateAndTaskDoesNotExist() throws Exception {
        String requestBody = """
                {
                    "title": "feed the cat",
                    "description": "grab the feed from the table",
                    "priority": "HIGH"
                }
                """;

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSetTaskStatus() throws Exception {
        Project project = projectRepository.saveAndFlush(
                new Project("home", "home tasks", ProjectStatus.ACTIVE)
        );

        Task task = taskRepository.save(
                new Task(
                        "feed the cat",
                        "",
                        TaskStatus.TODO,
                        TaskPriority.HIGH,
                        project
                )
        );

        String requestBody = """
                {
                    "status": "DONE"
                }
                """;

        mockMvc.perform(patch("/api/tasks/{id}/status", task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("feed the cat"))
                .andExpect(jsonPath("$.description").value(""))
                .andExpect(jsonPath("$.status").value("DONE"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test
    void shouldReturnNotFoundWhenPatchAndTaskDoesNotExist() throws Exception {
        String requestBody = """
                {
                    "status": "DONE"
                }
                """;

        mockMvc.perform(patch("/api/tasks/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteTaskById() throws Exception {
        Project project = projectRepository.saveAndFlush(
                new Project("home", "home tasks", ProjectStatus.ACTIVE)
        );

        Task task = taskRepository.save(
                new Task(
                        "feed the cat",
                        "",
                        TaskStatus.TODO,
                        TaskPriority.HIGH,
                        project
                )
        );

        mockMvc.perform(delete("/api/tasks/{id}", task.getId()))
                .andExpect(status().isNoContent());

        assertThat(taskRepository.findById(task.getId())).isNotPresent();
    }

    @Test
    void shouldReturnNotFoundWhenDeleteAndTaskDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNotFound());
    }
}
