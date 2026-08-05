package com.example.tasktracker.entity;

public enum TaskPriority {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH");

    private final String value;

    TaskPriority(String value) {
        this.value = value;
    }
}
