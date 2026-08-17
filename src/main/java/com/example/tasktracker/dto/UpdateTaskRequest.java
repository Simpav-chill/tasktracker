package com.example.tasktracker.dto;

import com.example.tasktracker.entity.TaskPriority;

public record UpdateTaskRequest(
        String title,
        String description,
        TaskPriority priority
) {}