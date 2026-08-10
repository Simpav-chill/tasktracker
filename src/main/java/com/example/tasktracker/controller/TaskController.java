package com.example.tasktracker.controller;

import com.example.tasktracker.dto.TaskDto;
import com.example.tasktracker.dto.UpdateTaskDto;
import com.example.tasktracker.dto.UpdateTaskStatusDto;
import com.example.tasktracker.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskDto updateTaskInfo(@PathVariable Long id,
                                  @RequestBody @Valid UpdateTaskDto dto) {
        return taskService.updateTaskInfo(id, dto);
    }

    @PatchMapping("/{id}/status")
    public TaskDto setTaskStatus(@PathVariable Long id,
                                 @RequestBody @Valid UpdateTaskStatusDto dto) {
        return taskService.setTaskStatus(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaskById(@PathVariable Long id) {
        taskService.deleteTaskById(id);
    }
}
