package com.example.tasktracker.web.exception;

import com.example.tasktracker.exception.EntityAlreadyExistsException;
import com.example.tasktracker.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleNotFound(EntityNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );

        problem.setProperty("timestamp", LocalDateTime.now());

        return problem;
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ProblemDetail handleAlreadyExists(EntityAlreadyExistsException ex) {
       ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );

        problem.setProperty("timestamp", LocalDateTime.now());

        return problem;
    }
}
