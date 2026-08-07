package com.example.tasktracker.dto;

import com.example.tasktracker.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectDto {

    private String title;

    private String description;

    private ProjectStatus status;
}
