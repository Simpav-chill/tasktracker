package com.example.tasktracker.mapper;

import com.example.tasktracker.dto.TaskDto;
import com.example.tasktracker.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskDto toDto(Task task) {
        if (task == null) {
            return null;
        }

        TaskDto dto = new TaskDto();

        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());

        return dto;
    }

    public Task toEntity(TaskDto dto) {
        if (dto == null) {
            return null;
        }

        Task task = new Task();

        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());

        return task;
    }
}
