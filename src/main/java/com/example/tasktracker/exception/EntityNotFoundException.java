package com.example.tasktracker.exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String message) {
        super(message);
    }

    public static EntityNotFoundException forProjectId(long id) {
        return new EntityNotFoundException("Project with id " + id + " not found");
    }

    public static EntityNotFoundException forTaskId(long id) {
        return new EntityNotFoundException("Task with id " + id + " not found");
    }
}
