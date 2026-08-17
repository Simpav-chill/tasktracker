package com.example.tasktracker.unit;

import com.example.tasktracker.dto.TaskResponse;
import com.example.tasktracker.dto.UpdateTaskRequest;
import com.example.tasktracker.dto.UpdateTaskStatusRequest;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.entity.TaskPriority;
import com.example.tasktracker.entity.TaskStatus;
import com.example.tasktracker.exception.EntityNotFoundException;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceUnitTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getTaskByIdTest() {
        Task found = new Task(
                "buy groceries",
                "buy cheese, milk and bread",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );
        found.setId(1L);
        found.setCreatedAt(LocalDateTime.now());
        found.setUpdatedAt(LocalDateTime.now());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(found));

        TaskResponse mapped = new TaskResponse(
                1L,
                "buy groceries",
                "buy cheese, milk and bread",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );

        when(taskMapper.toDto(found)).thenReturn(mapped);

        TaskResponse result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("buy groceries", result.title());
        assertEquals("buy cheese, milk and bread", result.description());
        assertEquals(TaskStatus.TODO, result.status());
        assertEquals(TaskPriority.MEDIUM, result.priority());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskMapper, times(1)).toDto(found);
    }

    @Test
    void getTaskByIdExceptionTest() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> taskService.getTaskById(1L));

        assertEquals("Task 1 not found", exception.getMessage());
    }

    @Test
    void updateTaskInfoTest() {
        UpdateTaskRequest updateDto = new UpdateTaskRequest(
                "buy groceries",
                "buy only milk and bread, i've found cheese",
                TaskPriority.MEDIUM
        );

        Task found = new Task(
                "buy groceries",
                "buy cheese, milk and bread",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );
        found.setId(1L);
        found.setCreatedAt(LocalDateTime.now());
        found.setUpdatedAt(LocalDateTime.now());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(found));

        found.setTitle(updateDto.title());
        found.setDescription(updateDto.description());
        found.setPriority(updateDto.priority());

        when(taskRepository.save(any(Task.class))).thenReturn(found);

        TaskResponse mapped = new TaskResponse(
                1L,
                "buy groceries",
                "buy only milk and bread, i've found cheese",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );

        when(taskMapper.toDto(found)).thenReturn(mapped);

        TaskResponse result = taskService.updateTaskInfo(1L, updateDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("buy groceries", result.title());
        assertEquals("buy only milk and bread, i've found cheese", result.description());
        assertEquals(TaskStatus.TODO, result.status());
        assertEquals(TaskPriority.MEDIUM, result.priority());

        verify(taskRepository, times(1)).save(found);
        verify(taskMapper, times(1)).toDto(found);
    }

    @Test
    void updateTaskInfoExceptionTest() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateTaskRequest updateDto = new UpdateTaskRequest(
                "buy groceries",
                "buy only milk and bread, i've found cheese",
                TaskPriority.MEDIUM
        );

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> taskService.updateTaskInfo(1L, updateDto));

        assertEquals("Task 1 not found", exception.getMessage());
    }

    @Test
    void setTaskStatusTest() {
        UpdateTaskStatusRequest dto = new UpdateTaskStatusRequest(TaskStatus.DONE);

        Task found = new Task(
                "buy groceries",
                "buy cheese, milk and bread",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );
        found.setId(1L);
        found.setCreatedAt(LocalDateTime.now());
        found.setUpdatedAt(LocalDateTime.now());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(found));

        found.setStatus(dto.status());

        when(taskRepository.save(any(Task.class))).thenReturn(found);

        TaskResponse mapped = new TaskResponse(
                1L,
                "buy groceries",
                "buy cheese, milk and bread",
                TaskStatus.DONE,
                TaskPriority.MEDIUM
        );

        when(taskMapper.toDto(found)).thenReturn(mapped);

        TaskResponse result = taskService.setTaskStatus(1L, dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("buy groceries", result.title());
        assertEquals("buy cheese, milk and bread", result.description());
        assertEquals(TaskStatus.DONE, result.status());
        assertEquals(TaskPriority.MEDIUM, result.priority());

        verify(taskRepository, times(1)).save(found);
        verify(taskMapper, times(1)).toDto(found);
    }

    @Test
    void setTaskStatusExceptionTest() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateTaskStatusRequest dto = new UpdateTaskStatusRequest(TaskStatus.DONE);

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> taskService.setTaskStatus(1L, dto));

        assertEquals("Task 1 not found", exception.getMessage());
    }

    @Test
    void deleteTaskByIdTest() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTaskById(1L);

        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTaskByIdExceptionTest() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> taskService.deleteTaskById(1L));

        assertEquals("Task 1 not found", exception.getMessage());
    }
}
