package com.example.tasktracker.dto;

import com.example.tasktracker.entity.TaskStatus;

public record UpdateTaskStatusRequest(
        TaskStatus status
) {}