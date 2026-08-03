package com.example.tasktrackerrestart.repository;

import com.example.tasktrackerrestart.dto.ProjectDto;
import com.example.tasktrackerrestart.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Boolean existsProjectByTitle(String title);
}
