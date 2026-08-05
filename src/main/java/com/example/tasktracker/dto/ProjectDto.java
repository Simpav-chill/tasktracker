package com.example.tasktracker.dto;

import com.example.tasktracker.entity.ProjectStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private Long id;

    private String title;

    private String description;

    private ProjectStatus status;
}