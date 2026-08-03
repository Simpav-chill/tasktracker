package com.example.tasktrackerrestart.service;

import com.example.tasktrackerrestart.dto.ProjectDto;
import com.example.tasktrackerrestart.dto.TaskDto;
import com.example.tasktrackerrestart.entity.Project;
import com.example.tasktrackerrestart.entity.ProjectStatus;
import com.example.tasktrackerrestart.entity.Task;
import com.example.tasktrackerrestart.mapper.ProjectMapper;
import com.example.tasktrackerrestart.mapper.TaskMapper;
import com.example.tasktrackerrestart.repository.ProjectRepository;
import com.example.tasktrackerrestart.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    public ProjectDto createProject(ProjectDto dto) {
        if (projectRepository.existsProjectByTitle(dto.getTitle())) {
            throw new RuntimeException("Project already exists: " + dto.getTitle());
        }

        projectRepository.save(projectMapper.toEntity(dto));

        return dto;
    }

    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(projectMapper::toDto)
                .toList();
    }

    public ProjectDto getProjectById(Long id) {
        return projectMapper.toDto(projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found: " + id)));
    }

    public ProjectDto updateProjectInfo(Long id, ProjectDto updateDto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found: " + id));

        project.setTitle(updateDto.getTitle());
        project.setDescription(updateDto.getDescription());

        projectRepository.save(project);

        return projectMapper.toDto(project);
    }

    public void deleteProjectById(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found: " + id);
        }

        projectRepository.deleteById(id);
    }

    public ProjectDto archiveProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found: " + id));

        project.setStatus(ProjectStatus.ARCHIVE);

        projectRepository.save(project);
        
        return projectMapper.toDto(project);
    }

    public TaskDto createTask(Long projectId, TaskDto dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        Task task = taskMapper.toEntity(dto);

        project.addTask(task);

        Task saved = taskRepository.save(task);

        return taskMapper.toDto(saved);
    }

    public List<TaskDto> getTasksByProjectId(Long projectId) {
        return taskRepository.findAllByProjectId(projectId)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }
}
