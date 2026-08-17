package com.example.tasktracker.mapper;

import com.example.tasktracker.dto.CreateProjectRequest;
import com.example.tasktracker.dto.ProjectResponse;
import com.example.tasktracker.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectResponse toDto(Project project) {
        if (project == null) {
            return null;
        }

        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getStatus()
        );
    }

    public Project toEntity(CreateProjectRequest dto) {
        if (dto == null) {
            return null;
        }

        Project project = new Project();

        project.setTitle(dto.title());
        project.setDescription(dto.description());
        project.setStatus(dto.status());

        return project;
    }
}
