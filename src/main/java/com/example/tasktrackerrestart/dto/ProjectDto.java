package com.example.tasktrackerrestart.dto;

import com.example.tasktrackerrestart.entity.ProjectStatus;
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