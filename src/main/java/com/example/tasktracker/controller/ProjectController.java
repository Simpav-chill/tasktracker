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
    public ProjectDto createProject(@RequestBody @Valid CreateProjectDto dto) {
        return projectService.createProject(dto);
    }

    @GetMapping
    public List<ProjectDto> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ProjectDto getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PutMapping("/{id}")
    public ProjectDto updateProjectInfo(@PathVariable Long id,
                                 @RequestBody @Valid UpdateProjectDto updateDto) {
        return projectService.updateProjectInfo(id, updateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectById(@PathVariable Long id) {
        projectService.deleteProjectById(id);
    }

    @PatchMapping("/{id}/archive")
    public ProjectDto archiveProjectById(@PathVariable Long id) {
        return projectService.archiveProjectById(id);
    }

    @PostMapping("/{projectId}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto createTask(@PathVariable Long projectId,
                              @RequestBody @Valid CreateTaskDto dto) {
        return projectService.createTask(projectId, dto);
    }

    @GetMapping("/{projectId}/tasks")
    public List<TaskDto> getTasksByProjectId(@PathVariable Long projectId) {
        return projectService.getTasksByProjectId(projectId);
    }
}
