package com.example.tasktracker.controller;

import com.example.tasktracker.dto.*;
import com.example.tasktracker.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse createProject(@RequestBody @Valid CreateProjectRequest dto) {
        return projectService.createProject(dto);
    }

    @GetMapping
    public List<ProjectResponse> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ProjectResponse getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PutMapping("/{id}")
    public ProjectResponse updateProjectInfo(@PathVariable Long id,
                                             @RequestBody @Valid UpdateProjectRequest updateDto) {
        return projectService.updateProjectInfo(id, updateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectById(@PathVariable Long id) {
        projectService.deleteProjectById(id);
    }

    @PatchMapping("/{id}/archive")
    public ProjectResponse archiveProjectById(@PathVariable Long id) {
        return projectService.archiveProjectById(id);
    }

    @PostMapping("/{projectId}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@PathVariable Long projectId,
                                   @RequestBody @Valid CreateTaskRequest dto) {
        return projectService.createTask(projectId, dto);
    }

    @GetMapping("/{projectId}/tasks")
    public List<TaskResponse> getTasksByProjectId(@PathVariable Long projectId) {
        return projectService.getTasksByProjectId(projectId);
    }
}
