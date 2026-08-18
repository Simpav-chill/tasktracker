package com.example.tasktracker.service;

import com.example.tasktracker.dto.*;
import com.example.tasktracker.entity.Project;
import com.example.tasktracker.entity.ProjectStatus;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.exception.EntityAlreadyExistsException;
import com.example.tasktracker.exception.EntityNotFoundException;
import com.example.tasktracker.mapper.ProjectMapper;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.TaskRepository;
import jakarta.transaction.Transactional;
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

    public ProjectResponse createProject(CreateProjectRequest dto) {
        if (projectRepository.existsProjectByTitle(dto.title())) {
            throw new EntityAlreadyExistsException("Project with title '" + dto.title() + "' already exists");
        }

        Project saved = projectRepository.save(projectMapper.toEntity(dto));

        return projectMapper.toDto(saved);
    }

    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(projectMapper::toDto)
                .toList();
    }

    public ProjectResponse getProjectById(Long id) {
        return projectMapper.toDto(projectRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forProjectId(id)));
    }

    public ProjectResponse updateProjectInfo(Long id, UpdateProjectRequest updateDto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forProjectId(id));

        project.setTitle(updateDto.title());
        project.setDescription(updateDto.description());

        projectRepository.save(project);

        return projectMapper.toDto(project);
    }

    public void deleteProjectById(Long id) {
        if (!projectRepository.existsById(id)) {
            throw EntityNotFoundException.forProjectId(id);
        }

        projectRepository.deleteById(id);
    }

    public ProjectResponse archiveProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forProjectId(id));

        project.setStatus(ProjectStatus.ARCHIVE);

        projectRepository.save(project);
        
        return projectMapper.toDto(project);
    }

    @Transactional
    public TaskResponse createTask(Long projectId, CreateTaskRequest dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> EntityNotFoundException.forProjectId(projectId));

        Task task = taskMapper.toEntity(dto);

        project.addTask(task);

        Task saved = taskRepository.save(task);

        return taskMapper.toDto(saved);
    }

    public List<TaskResponse> getTasksByProjectId(Long projectId) {
        return taskRepository.findAllByProjectId(projectId)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }
}
