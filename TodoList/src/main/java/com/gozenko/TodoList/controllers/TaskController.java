package com.gozenko.TodoList.controllers;

import com.gozenko.TodoList.DTO.TaskRequest;
import com.gozenko.TodoList.DTO.TaskResponse;
import com.gozenko.TodoList.models.Task;
import com.gozenko.TodoList.services.TaskService;
import com.gozenko.TodoList.utils.Priority;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskRequest taskReq) {
        try {
            Task task = new Task();
            task.setNaming(taskReq.getNaming());

            Priority priority = Priority.fromString(taskReq.getPriority());
            task.setPriority(priority);

            Task createdTask;
            if (taskReq.getUserId() != null) {
                createdTask = taskService.createTaskWithUser(task, taskReq.getUserId());
            } else {
                createdTask = taskService.createTaskWithoutUser(task);
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new TaskResponse(createdTask));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Пользователь не найден: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка в данных: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка сервера: " + e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getTaskById(@RequestParam Integer id) {
        try {
            Task task = taskService.findById(id);
            return ResponseEntity.ok(new TaskResponse(task));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllTasksByUser(@RequestParam(required = false) Integer userId) {
        try {
            if (userId != null) {
                List<Task> tasks = taskService.findByUserId(userId);
                return ResponseEntity.ok(TaskResponse.fromTasks(tasks));
            } else {
                List<Task> tasks = taskService.getAll();
                return ResponseEntity.ok(TaskResponse.fromTasks(tasks));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}