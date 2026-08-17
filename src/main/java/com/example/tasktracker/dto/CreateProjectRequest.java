package com.example.tasktracker.dto;

import com.example.tasktracker.entity.ProjectStatus;

public record CreateProjectRequest(
        String title,
        String description,
        ProjectStatus status
) {}