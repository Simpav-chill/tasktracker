package com.example.tasktracker.dto;

import com.example.tasktracker.entity.TaskPriority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskDto {

    private String title;

    private String description;

    private TaskPriority priority;
}
