package com.example.tasktrackerrestart.entity;

public enum ProjectStatus {
    ACTIVE("ACTIVE"),
    ARCHIVE("ARCHIVE");


    private final String value;

    ProjectStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
