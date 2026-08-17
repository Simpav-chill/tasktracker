package com.example.tasktracker.dto;

import com.example.tasktracker.entity.TaskPriority;
import com.example.tasktracker.entity.TaskStatus;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority
) {}