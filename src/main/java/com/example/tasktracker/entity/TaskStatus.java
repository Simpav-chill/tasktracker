package com.example.tasktracker.entity;

public enum TaskStatus {
    TODO("TODO"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    private final String value;

    TaskStatus(String value) {
        this.value = value;
    }
}
