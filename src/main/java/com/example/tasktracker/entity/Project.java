package com.example.tasktracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private ProjectStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    public Project() {}

    public Project(String title, String description, ProjectStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Project(String title, String description, ProjectStatus status, List<Task> tasks) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.tasks = tasks != null ? tasks : new ArrayList<>();
    }

    public void addTask(Task task) {
        this.tasks.add(task);
        task.setProject(this);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
        task.setProject(null);
    }
}
