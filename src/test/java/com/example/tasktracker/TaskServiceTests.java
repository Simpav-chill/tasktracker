package com.example.tasktracker;

import com.example.tasktracker.dto.TaskDto;
import com.example.tasktracker.dto.UpdateTaskDto;
import com.example.tasktracker.dto.UpdateTaskStatusDto;
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
class TaskServiceTests {

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

        TaskDto mapped = new TaskDto(
                1L,
                "buy groceries",
                "buy cheese, milk and bread",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );

        when(taskMapper.toDto(found)).thenReturn(mapped);

        TaskDto result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("buy groceries", result.getTitle());
        assertEquals("buy cheese, milk and bread", result.getDescription());
        assertEquals(TaskStatus.TODO, result.getStatus());
        assertEquals(TaskPriority.MEDIUM, result.getPriority());

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
        UpdateTaskDto updateDto = new UpdateTaskDto(
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

        found.setTitle(updateDto.getTitle());
        found.setDescription(updateDto.getDescription());
        found.setPriority(updateDto.getPriority());

        when(taskRepository.save(any(Task.class))).thenReturn(found);

        TaskDto mapped = new TaskDto(
                1L,
                "buy groceries",
                "buy only milk and bread, i've found cheese",
                TaskStatus.TODO,
                TaskPriority.MEDIUM
        );

        when(taskMapper.toDto(found)).thenReturn(mapped);

        TaskDto result = taskService.updateTaskInfo(1L, updateDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("buy groceries", result.getTitle());
        assertEquals("buy only milk and bread, i've found cheese", result.getDescription());
        assertEquals(TaskStatus.TODO, result.getStatus());
        assertEquals(TaskPriority.MEDIUM, result.getPriority());

        verify(taskRepository, times(1)).save(found);
        verify(taskMapper, times(1)).toDto(found);
    }

    @Test
    void updateTaskInfoExceptionTest() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateTaskDto updateDto = new UpdateTaskDto(
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
        UpdateTaskStatusDto dto = new UpdateTaskStatusDto(TaskStatus.DONE);

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

        found.setStatus(dto.getStatus());

        when(taskRepository.save(any(Task.class))).thenReturn(found);

        TaskDto mapped = new TaskDto(
                1L,
                "buy groceries",
                "buy cheese, milk and bread",
                TaskStatus.DONE,
                TaskPriority.MEDIUM
        );

        when(taskMapper.toDto(found)).thenReturn(mapped);

        TaskDto result = taskService.setTaskStatus(1L, dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("buy groceries", result.getTitle());
        assertEquals("buy cheese, milk and bread", result.getDescription());
        assertEquals(TaskStatus.DONE, result.getStatus());
        assertEquals(TaskPriority.MEDIUM, result.getPriority());

        verify(taskRepository, times(1)).save(found);
        verify(taskMapper, times(1)).toDto(found);
    }

    @Test
    void setTaskStatusExceptionTest() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateTaskStatusDto dto = new UpdateTaskStatusDto(TaskStatus.DONE);

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
