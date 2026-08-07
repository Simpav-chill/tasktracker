package com.example.tasktracker.mapper;

import com.example.tasktracker.dto.CreateProjectDto;
import com.example.tasktracker.dto.ProjectDto;
import com.example.tasktracker.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectDto toDto(Project project) {
        if (project == null) {
            return null;
        }

        ProjectDto dto = new ProjectDto();

        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setStatus(project.getStatus());

        return dto;
    }

    public Project toEntity(CreateProjectDto dto) {
        if (dto == null) {
            return null;
        }

        Project project = new Project();

        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setStatus(dto.getStatus());

        return project;
    }
}
