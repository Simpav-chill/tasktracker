package com.example.tasktracker.service;

import com.example.tasktracker.dto.TaskResponse;
import com.example.tasktracker.dto.UpdateTaskRequest;
import com.example.tasktracker.dto.UpdateTaskStatusRequest;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.exception.EntityNotFoundException;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskResponse getTaskById(Long id) {
        return taskMapper.toDto(taskRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forTaskId(id)));
    }

    public TaskResponse updateTaskInfo(Long id, UpdateTaskRequest dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forTaskId(id));

        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setPriority(dto.priority());

        taskRepository.save(task);

        return taskMapper.toDto(task);
    }

    public TaskResponse setTaskStatus(Long id, UpdateTaskStatusRequest dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forTaskId(id));

        task.setStatus(dto.status());

        taskRepository.save(task);

        return taskMapper.toDto(task);
    }

    public void deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw EntityNotFoundException.forTaskId(id);
        }

        taskRepository.deleteById(id);
    }
}
