package com.example.tasktracker.unit;

import com.example.tasktracker.dto.*;
import com.example.tasktracker.entity.*;
import com.example.tasktracker.exception.EntityAlreadyExistsException;
import com.example.tasktracker.exception.EntityNotFoundException;
import com.example.tasktracker.mapper.ProjectMapper;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceUnitTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void createProjectTest() {
        CreateProjectDto dto = new CreateProjectDto(
                "home",
                "home tasks",
                ProjectStatus.ACTIVE);

        Project mapped = new Project(
                "home",
                "home tasks",
                ProjectStatus.ACTIVE
        );

        when(projectMapper.toEntity(dto)).thenReturn(mapped);

        Project saved = new Project(
                "home",
                "home tasks",
                ProjectStatus.ACTIVE);
        saved.setId(1L);

        when(projectRepository.existsProjectByTitle("home")).thenReturn(false);
        when(projectRepository.save(any(Project.class))).thenReturn(saved);

        ProjectDto returnResult = new ProjectDto(
                1L,
                "home",
                "home tasks",
                ProjectStatus.ACTIVE);

        when(projectMapper.toDto(saved)).thenReturn(returnResult);

        ProjectDto result = projectService.createProject(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("home", result.getTitle());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void createProjectExceptionTest() {
        CreateProjectDto dto = new CreateProjectDto("home",
                "home tasks",
                ProjectStatus.ACTIVE);

        when(projectRepository.existsProjectByTitle("home")).thenReturn(true);

        Exception exception = assertThrows(EntityAlreadyExistsException.class,
                () -> projectService.createProject(dto));

        assertEquals("Project with title '" + dto.getTitle() + "' already exists",
                exception.getMessage());
    }

    @Test
    void getAllProjectsTest() {
        Project firstProject = new Project(
                "home",
                "home tasks",
                ProjectStatus.ACTIVE
        );

        firstProject.setId(1L);
        firstProject.setCreatedAt(LocalDateTime.now());
        firstProject.setUpdatedAt(LocalDateTime.now());

        Project secondProject = new Project(
                "work",
                "work tasks",
                ProjectStatus.ACTIVE
        );

        secondProject.setId(2L);
        secondProject.setCreatedAt(LocalDateTime.now());
        secondProject.setUpdatedAt(LocalDateTime.now());

        Project thirdProject = new Project(
                "gym",
                "exercises",
                ProjectStatus.ARCHIVE
        );

        thirdProject.setId(3L);
        thirdProject.setCreatedAt(LocalDateTime.now());
        thirdProject.setUpdatedAt(LocalDateTime.now());

        when(projectRepository.findAll())
                .thenReturn(List.of(firstProject, secondProject, thirdProject));

        ProjectDto firstDto = new ProjectDto(
                1L,
                "home",
                "home tasks",
                ProjectStatus.ACTIVE
        );
        ProjectDto secondDto = new ProjectDto(
                2L,
                "work",
                "work tasks",
                ProjectStatus.ACTIVE
        );
        ProjectDto thirdDto = new ProjectDto(
                3L,
                "gym",
                "exercises",
                ProjectStatus.ARCHIVE
        );

        when(projectMapper.toDto(firstProject))
                .thenReturn(firstDto);
        when(projectMapper.toDto(secondProject))
                .thenReturn(secondDto);
        when(projectMapper.toDto(thirdProject))
                .thenReturn(thirdDto);

        List<ProjectDto> result = projectService.getAllProjects();

        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals("home", result.get(0).getTitle());
        assertEquals("home tasks", result.get(0).getDescription());
        assertEquals(ProjectStatus.ACTIVE, result.get(0).getStatus());

        assertEquals("work", result.get(1).getTitle());
        assertEquals("work tasks", result.get(1).getDescription());
        assertEquals(ProjectStatus.ACTIVE, result.get(1).getStatus());

        assertEquals("gym", result.get(2).getTitle());
        assertEquals("exercises", result.get(2).getDescription());
        assertEquals(ProjectStatus.ARCHIVE, result.get(2).getStatus());

        verify(projectMapper, times(1)).toDto(firstProject);
        verify(projectMapper, times(1)).toDto(secondProject);
        verify(projectMapper, times(1)).toDto(thirdProject);
    }

    @Test
    void getAllProjectsDontExistTest() {
        when(projectRepository.findAll()).thenReturn(List.of());

        List<ProjectDto> result = projectService.getAllProjects();

        assertNotNull(result);
        assertEquals(List.of(), result);
    }

    @Test
    void getProjectByIdTest() {
        Long id = 1L;

        Project found = new Project(
                "home",
                "home tasks",
                ProjectStatus.ACTIVE
        );
        found.setId(id);
        found.setCreatedAt(LocalDateTime.now());
        found.setUpdatedAt(LocalDateTime.now());

        when(projectRepository.findById(id)).thenReturn(Optional.of(found));

        ProjectDto mapped = new ProjectDto(
                id,
                "home",
                "home tasks",
                ProjectStatus.ACTIVE
        );

        when(projectMapper.toDto(found)).thenReturn(mapped);

        ProjectDto result = projectService.getProjectById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("home", result.getTitle());
        assertEquals("home tasks", result.getDescription());
        assertEquals(ProjectStatus.ACTIVE, result.getStatus());

        verify(projectMapper, times(1)).toDto(found);
    }

    @Test
    void getProjectByIdExceptionTest() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> projectService.getProjectById(1L));

        assertEquals("Project with id 1 not found", exception.getMessage());
    }

    @Test
    void updateProjectInfoTest() {
        UpdateProjectDto updateDto = new UpdateProjectDto(
                "work",
                "work tasks"
        );

        Project found = new Project(
                "home",
                "home tasks",
                ProjectStatus.ACTIVE
        );
        found.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(found));

        found.setTitle(updateDto.getTitle());
        found.setDescription(updateDto.getDescription());

        when(projectRepository.save(any(Project.class))).thenReturn(found);

        ProjectDto mapped = new ProjectDto(
                1L,
                "work",
                "work tasks",
                ProjectStatus.ACTIVE
        );

        when(projectMapper.toDto(found)).thenReturn(mapped);

        ProjectDto result = projectService.updateProjectInfo(1L, updateDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("work", result.getTitle());
        assertEquals("work tasks", result.getDescription());

        verify(projectRepository).save(found);
        verify(projectMapper, times(1)).toDto(found);
    }

    @Test
    void updateProjectInfoExceptionTest() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateProjectDto updateDto = new UpdateProjectDto("work", "work tasks");

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> projectService.updateProjectInfo(1L, updateDto));

        assertEquals("Project with id 1 not found", exception.getMessage());
    }

    @Test
    void deleteProjectByIdTest() {
        when(projectRepository.existsById(1L)).thenReturn(true);

        projectService.deleteProjectById(1L);

        verify(projectRepository,times(1)).existsById(1L);
        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProjectByIdExceptionTest() {
        when(projectRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> projectService.deleteProjectById(1L));

        assertEquals("Project with id 1 not found", exception.getMessage());
    }

    @Test
    void archiveProjectByIdTest() {
        Project found = new Project(
                "home",
                "home tasks",
                ProjectStatus.ACTIVE
        );
        found.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(found));

        found.setStatus(ProjectStatus.ARCHIVE);

        when(projectRepository.save(any(Project.class))).thenReturn(found);

        ProjectDto mapped = new ProjectDto(
                1L,
                "home",
                "home tasks",
                ProjectStatus.ARCHIVE
        );

        when(projectMapper.toDto(found)).thenReturn(mapped);

        ProjectDto result = projectService.archiveProjectById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("home", result.getTitle());
        assertEquals("home tasks", result.getDescription());
        assertEquals(ProjectStatus.ARCHIVE, result.getStatus());

        verify(projectRepository, times(1)).save(any(Project.class));
        verify(projectMapper, times(1)).toDto(found);
    }

    @Test
    void  archiveProjectByIdExceptionTest() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> projectService.archiveProjectById(1L));

        assertEquals("Project with id 1 not found", exception.getMessage());
    }

    @Test
    void createTaskTest() {
        Long projectId = 1L;

        Project found = new Project(
                "home",
                "home tasks",
                ProjectStatus.ACTIVE
        );
        found.setId(1L);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(found));

        CreateTaskDto dto = new CreateTaskDto(
                "clean the room",
                "vacuum clean and wash the floor",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );

        Task task = new Task(
                "clean the room",
                "vacuum clean and wash the floor",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );
        task.setId(1L);

        when(taskMapper.toEntity(dto)).thenReturn(task);

        Task saved = new Task(
                "clean the room",
                "vacuum clean and wash the floor",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );
        saved.setId(1L);
        saved.setCreatedAt(LocalDateTime.now());
        saved.setUpdatedAt(LocalDateTime.now());

        when(taskRepository.save(task)).thenReturn(saved);

        TaskDto mapped = new TaskDto(
                1L,
                "clean the room",
                "vacuum clean and wash the floor",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );

        when(taskMapper.toDto(saved)).thenReturn(mapped);

        TaskDto result = projectService.createTask(projectId, dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("clean the room", result.getTitle());
        assertEquals("vacuum clean and wash the floor", result.getDescription());
        assertEquals(TaskStatus.TODO, result.getStatus());
        assertEquals(TaskPriority.MEDIUM, result.getPriority());

        verify(taskMapper, times(1)).toEntity(dto);
        verify(taskRepository, times(1)).save(task);
        verify(taskMapper, times(1)).toDto(saved);
    }

    @Test
    void createTaskExceptionTest() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        CreateTaskDto dto = new CreateTaskDto(
                "clean the room",
                "vacuum clean and wash the floor",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> projectService.createTask(1L, dto));

        assertEquals("Project with id 1 not found", exception.getMessage());
    }

    @Test
    void getTasksByProjectIdTest() {
        Task firstTask = new Task(
                "clean desk",
                "",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );
        firstTask.setId(1L);
        firstTask.setCreatedAt(LocalDateTime.now());
        firstTask.setUpdatedAt(LocalDateTime.now());
        Task secondTask = new Task(
                "meeting",
                "at 4 pm",
                TaskStatus.TODO,
                TaskPriority.HIGH
        );
        secondTask.setId(2L);
        secondTask.setCreatedAt(LocalDateTime.now());
        secondTask.setUpdatedAt(LocalDateTime.now());
        Task thirdTask = new Task(
                "water plants",
                "grab watering can",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );
        thirdTask.setId(3L);
        thirdTask.setCreatedAt(LocalDateTime.now());
        thirdTask.setUpdatedAt(LocalDateTime.now());

        when(taskRepository.findAllByProjectId(1L))
                .thenReturn(List.of(firstTask, secondTask, thirdTask));

        TaskDto firstDto = new TaskDto(
                1L,
                "clean desk",
                "",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );
        TaskDto secondDto = new TaskDto(
                2L,
                "meeting",
                "at 4 pm",
                TaskStatus.TODO,
                TaskPriority.HIGH
        );
        TaskDto thirdDto = new TaskDto(
                3L,
                "water plants",
                "grab watering can",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );

        when(taskMapper.toDto(firstTask)).thenReturn(firstDto);
        when(taskMapper.toDto(secondTask)).thenReturn(secondDto);
        when(taskMapper.toDto(thirdTask)).thenReturn(thirdDto);

        List<TaskDto> result = projectService.getTasksByProjectId(1L);

        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("clean desk", result.get(0).getTitle());
        assertEquals("", result.get(0).getDescription());
        assertEquals(TaskStatus.TODO, result.get(0).getStatus());
        assertEquals(TaskPriority.MEDIUM, result.get(0).getPriority());

        assertEquals(2L, result.get(1).getId());
        assertEquals("meeting", result.get(1).getTitle());
        assertEquals("at 4 pm", result.get(1).getDescription());
        assertEquals(TaskStatus.TODO, result.get(1).getStatus());
        assertEquals(TaskPriority.HIGH, result.get(1).getPriority());

        assertEquals(3L, result.get(2).getId());
        assertEquals("water plants", result.get(2).getTitle());
        assertEquals("grab watering can", result.get(2).getDescription());
        assertEquals(TaskStatus.TODO, result.get(2).getStatus());
        assertEquals(TaskPriority.MEDIUM, result.get(2).getPriority());

        verify(taskMapper, times(1)).toDto(firstTask);
        verify(taskMapper, times(1)).toDto(secondTask);
        verify(taskMapper, times(1)).toDto(thirdTask);
    }

    @Test
    void getTasksByProjectIdDontExistTest() {
        when(taskRepository.findAllByProjectId(1L)).thenReturn(List.of());

        List<TaskDto> result = projectService.getTasksByProjectId(1L);

        assertNotNull(result);
        assertEquals(List.of(), result);
    }
}
