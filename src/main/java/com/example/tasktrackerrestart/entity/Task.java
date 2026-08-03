package com.example.tasktrackerrestart.entity;

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
public class Task {

    @Id
    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @CreatedDate
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;

    public Task() {}

    Task(Long id, String title, String description, TaskPriority priority) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.TODO;
        this.priority = priority;
    }

    Task(Long id, String title, String description, TaskPriority priority, Project project) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.TODO;
        this.priority = priority;
        this.project = project;
    }
}
