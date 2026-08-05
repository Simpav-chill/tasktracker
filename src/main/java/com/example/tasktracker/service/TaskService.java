package com.example.tasktracker.service;

import com.example.tasktracker.dto.TaskDto;
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

    public TaskDto getTaskById(Long id) {
        return taskMapper.toDto(taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task " + id + " not found")));
    }

    public TaskDto updateTaskInfo(Long id, TaskDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task " + id + " not found"));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());

        taskRepository.save(task);

        return taskMapper.toDto(task);
    }

    public TaskDto setTaskStatus(Long id, TaskDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task " + id + " not found"));

        task.setStatus(dto.getStatus());

        taskRepository.save(task);

        return taskMapper.toDto(task);
    }

    public void deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task " + id + " not found");
        }

        taskRepository.deleteById(id);
    }
}
