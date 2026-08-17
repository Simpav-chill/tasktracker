package com.example.tasktracker.dto;

import com.example.tasktracker.entity.ProjectStatus;

public record ProjectResponse(
        Long id,
        String title,
        String description,
        ProjectStatus status
) {}