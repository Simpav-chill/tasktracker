package com.example.tasktrackerrestart.dto;

import com.example.tasktrackerrestart.entity.TaskPriority;
import com.example.tasktrackerrestart.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;
}