package com.example.tasktrackerrestart.service;

import com.example.tasktrackerrestart.dto.TaskDto;
import com.example.tasktrackerrestart.entity.Task;
import com.example.tasktrackerrestart.mapper.TaskMapper;
import com.example.tasktrackerrestart.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskDto getTaskById(Long id) {
        return taskMapper.toDto(taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id)));
    }

    public TaskDto updateTaskInfo(Long id, TaskDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());

        taskRepository.save(task);

        return taskMapper.toDto(task);
    }

    public TaskDto setTaskStatus(Long id, TaskDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(dto.getStatus());

        taskRepository.save(task);

        return taskMapper.toDto(task);
    }

    public void deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found: " + id);
        }

        taskRepository.deleteById(id);
    }
}
