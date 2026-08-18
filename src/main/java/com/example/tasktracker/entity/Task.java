package com.example.tasktracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Task() {}

    public Task(String title, String description, TaskStatus status, TaskPriority priority) {
        this(title, description, status, priority, null);
    }

    public Task(String title, String description, TaskStatus status, TaskPriority priority, Project project) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.project = project;
    }
}
