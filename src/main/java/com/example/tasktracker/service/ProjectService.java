package com.example.tasktracker.service;

import com.example.tasktracker.dto.ProjectDto;
import com.example.tasktracker.dto.TaskDto;
import com.example.tasktracker.entity.Project;
import com.example.tasktracker.entity.ProjectStatus;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.exception.EntityAlreadyExistsException;
import com.example.tasktracker.exception.EntityNotFoundException;
import com.example.tasktracker.mapper.ProjectMapper;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.TaskRepository;
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
            throw new EntityAlreadyExistsException("Project with title '" + dto.getTitle() + "' already exists");
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
                .orElseThrow(() -> new EntityNotFoundException("Project " + id + " not found")));
    }

    public ProjectDto updateProjectInfo(Long id, ProjectDto updateDto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project " + id + " not found"));

        project.setTitle(updateDto.getTitle());
        project.setDescription(updateDto.getDescription());

        projectRepository.save(project);

        return projectMapper.toDto(project);
    }

    public void deleteProjectById(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new EntityNotFoundException("Project " + id + " not found");
        }

        projectRepository.deleteById(id);
    }

    public ProjectDto archiveProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project " + id + " not found"));

        project.setStatus(ProjectStatus.ARCHIVE);

        projectRepository.save(project);
        
        return projectMapper.toDto(project);
    }

    public TaskDto createTask(Long projectId, TaskDto dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project " + projectId + " not found"));

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
