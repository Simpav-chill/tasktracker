package com.example.tasktracker.dto;

import com.example.tasktracker.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskStatusDto {

    private TaskStatus status;
}
