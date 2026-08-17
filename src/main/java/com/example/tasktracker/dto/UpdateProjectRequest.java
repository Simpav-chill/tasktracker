package com.example.tasktracker.dto;

public record UpdateProjectRequest(
        String title,
        String description
) {}
