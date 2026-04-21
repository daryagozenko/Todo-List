package com.gozenko.TodoList.controllers;

import com.gozenko.TodoList.models.Task;
import com.gozenko.TodoList.utils.Priority;
import com.gozenko.TodoList.services.TaskService;
import com.gozenko.TodoList.DTO.TaskRequest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private Task task;
    private TaskRequest taskRequest;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1);
        task.setNaming("Test Task");
        task.setPriority(Priority.HIGH);

        taskRequest = new TaskRequest();
        taskRequest.setNaming("Test Task");
        taskRequest.setPriority("Высокий");
        taskRequest.setUserId(1);
    }

    @Test
    void createTask_Success(){
        when(taskService.createTaskWithUser(any(Task.class), any(Integer.class))).thenReturn(task);

        ResponseEntity<?> response = taskController.createTask(taskRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void createTask_UserNotFound(){
        when(taskService.createTaskWithUser(any(Task.class), any(Integer.class)))
                .thenThrow(new EntityNotFoundException("User not found"));

        ResponseEntity<?> response = taskController.createTask(taskRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getTaskById_Success(){
        when(taskService.findById(any(Integer.class))).thenReturn(task);

        ResponseEntity<?> response = taskController.getTaskById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getTaskById_NotFound(){
        when(taskService.findById(any(Integer.class))).thenThrow(new EntityNotFoundException("Task not found"));

        ResponseEntity<?> response = taskController.getTaskById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}