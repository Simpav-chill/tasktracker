package com.example.tasktracker.dto;

import com.example.tasktracker.entity.TaskPriority;
import com.example.tasktracker.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskDto {

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;
}
