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
        CreateProjectRequest dto = new CreateProjectRequest(
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

        ProjectResponse returnResult = new ProjectResponse(
                1L,
                "home",
                "home tasks",
                ProjectStatus.ACTIVE);

        when(projectMapper.toDto(saved)).thenReturn(returnResult);

        ProjectResponse result = projectService.createProject(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("home", result.title());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void createProjectExceptionTest() {
        CreateProjectRequest dto = new CreateProjectRequest("home",
                "home tasks",
                ProjectStatus.ACTIVE);

        when(projectRepository.existsProjectByTitle("home")).thenReturn(true);

        Exception exception = assertThrows(EntityAlreadyExistsException.class,
                () -> projectService.createProject(dto));

        assertEquals("Project with title '" + dto.title() + "' already exists",
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

        ProjectResponse firstDto = new ProjectResponse(
                1L,
                "home",
                "home tasks",
                ProjectStatus.ACTIVE
        );
        ProjectResponse secondDto = new ProjectResponse(
                2L,
                "work",
                "work tasks",
                ProjectStatus.ACTIVE
        );
        ProjectResponse thirdDto = new ProjectResponse(
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

        List<ProjectResponse> result = projectService.getAllProjects();

        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals("home", result.get(0).title());
        assertEquals("home tasks", result.get(0).description());
        assertEquals(ProjectStatus.ACTIVE, result.get(0).status());

        assertEquals("work", result.get(1).title());
        assertEquals("work tasks", result.get(1).description());
        assertEquals(ProjectStatus.ACTIVE, result.get(1).status());

        assertEquals("gym", result.get(2).title());
        assertEquals("exercises", result.get(2).description());
        assertEquals(ProjectStatus.ARCHIVE, result.get(2).status());

        verify(projectMapper, times(1)).toDto(firstProject);
        verify(projectMapper, times(1)).toDto(secondProject);
        verify(projectMapper, times(1)).toDto(thirdProject);
    }

    @Test
    void getAllProjectsDontExistTest() {
        when(projectRepository.findAll()).thenReturn(List.of());

        List<ProjectResponse> result = projectService.getAllProjects();

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

        ProjectResponse mapped = new ProjectResponse(
                id,
                "home",
                "home tasks",
                ProjectStatus.ACTIVE
        );

        when(projectMapper.toDto(found)).thenReturn(mapped);

        ProjectResponse result = projectService.getProjectById(id);

        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("home", result.title());
        assertEquals("home tasks", result.description());
        assertEquals(ProjectStatus.ACTIVE, result.status());

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
        UpdateProjectRequest updateDto = new UpdateProjectRequest(
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

        found.setTitle(updateDto.title());
        found.setDescription(updateDto.description());

        when(projectRepository.save(any(Project.class))).thenReturn(found);

        ProjectResponse mapped = new ProjectResponse(
                1L,
                "work",
                "work tasks",
                ProjectStatus.ACTIVE
        );

        when(projectMapper.toDto(found)).thenReturn(mapped);

        ProjectResponse result = projectService.updateProjectInfo(1L, updateDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("work", result.title());
        assertEquals("work tasks", result.description());

        verify(projectRepository).save(found);
        verify(projectMapper, times(1)).toDto(found);
    }

    @Test
    void updateProjectInfoExceptionTest() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateProjectRequest updateDto = new UpdateProjectRequest("work", "work tasks");

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

        ProjectResponse mapped = new ProjectResponse(
                1L,
                "home",
                "home tasks",
                ProjectStatus.ARCHIVE
        );

        when(projectMapper.toDto(found)).thenReturn(mapped);

        ProjectResponse result = projectService.archiveProjectById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("home", result.title());
        assertEquals("home tasks", result.description());
        assertEquals(ProjectStatus.ARCHIVE, result.status());

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

        CreateTaskRequest dto = new CreateTaskRequest(
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

        TaskResponse mapped = new TaskResponse(
                1L,
                "clean the room",
                "vacuum clean and wash the floor",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );

        when(taskMapper.toDto(saved)).thenReturn(mapped);

        TaskResponse result = projectService.createTask(projectId, dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("clean the room", result.title());
        assertEquals("vacuum clean and wash the floor", result.description());
        assertEquals(TaskStatus.TODO, result.status());
        assertEquals(TaskPriority.MEDIUM, result.priority());

        verify(taskMapper, times(1)).toEntity(dto);
        verify(taskRepository, times(1)).save(task);
        verify(taskMapper, times(1)).toDto(saved);
    }

    @Test
    void createTaskExceptionTest() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        CreateTaskRequest dto = new CreateTaskRequest(
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

        TaskResponse firstDto = new TaskResponse(
                1L,
                "clean desk",
                "",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );
        TaskResponse secondDto = new TaskResponse(
                2L,
                "meeting",
                "at 4 pm",
                TaskStatus.TODO,
                TaskPriority.HIGH
        );
        TaskResponse thirdDto = new TaskResponse(
                3L,
                "water plants",
                "grab watering can",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );

        when(taskMapper.toDto(firstTask)).thenReturn(firstDto);
        when(taskMapper.toDto(secondTask)).thenReturn(secondDto);
        when(taskMapper.toDto(thirdTask)).thenReturn(thirdDto);

        List<TaskResponse> result = projectService.getTasksByProjectId(1L);

        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals(1L, result.get(0).id());
        assertEquals("clean desk", result.get(0).title());
        assertEquals("", result.get(0).description());
        assertEquals(TaskStatus.TODO, result.get(0).status());
        assertEquals(TaskPriority.MEDIUM, result.get(0).priority());

        assertEquals(2L, result.get(1).id());
        assertEquals("meeting", result.get(1).title());
        assertEquals("at 4 pm", result.get(1).description());
        assertEquals(TaskStatus.TODO, result.get(1).status());
        assertEquals(TaskPriority.HIGH, result.get(1).priority());

        assertEquals(3L, result.get(2).id());
        assertEquals("water plants", result.get(2).title());
        assertEquals("grab watering can", result.get(2).description());
        assertEquals(TaskStatus.TODO, result.get(2).status());
        assertEquals(TaskPriority.MEDIUM, result.get(2).priority());

        verify(taskMapper, times(1)).toDto(firstTask);
        verify(taskMapper, times(1)).toDto(secondTask);
        verify(taskMapper, times(1)).toDto(thirdTask);
    }

    @Test
    void getTasksByProjectIdDontExistTest() {
        when(taskRepository.findAllByProjectId(1L)).thenReturn(List.of());

        List<TaskResponse> result = projectService.getTasksByProjectId(1L);

        assertNotNull(result);
        assertEquals(List.of(), result);
    }
}
