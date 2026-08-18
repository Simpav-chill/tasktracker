package com.example.tasktracker.web.controller;

import com.example.tasktracker.dto.TaskResponse;
import com.example.tasktracker.dto.UpdateTaskRequest;
import com.example.tasktracker.dto.UpdateTaskStatusRequest;
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
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTaskInfo(@PathVariable Long id,
                                       @RequestBody @Valid UpdateTaskRequest dto) {
        return taskService.updateTaskInfo(id, dto);
    }

    @PatchMapping("/{id}/status")
    public TaskResponse setTaskStatus(@PathVariable Long id,
                                      @RequestBody @Valid UpdateTaskStatusRequest dto) {
        return taskService.setTaskStatus(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaskById(@PathVariable Long id) {
        taskService.deleteTaskById(id);
    }
}
