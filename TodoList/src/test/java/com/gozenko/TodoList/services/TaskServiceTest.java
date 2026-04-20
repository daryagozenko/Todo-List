package com.gozenko.TodoList.services;

import com.gozenko.TodoList.models.Task;
import com.gozenko.TodoList.models.User;
import com.gozenko.TodoList.repositories.TaskRepository;
import com.gozenko.TodoList.utils.Priority;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1, "Dasha", "Gozenko", new ArrayList<>());
        task = new Task(1,"Learn CI/CD", LocalDate.now(), Priority.HIGH, user);
    }

    @Test
    void createTaskWithUser_Success() {
        when(userService.findById(any(Integer.class))).thenReturn(user);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTaskWithUser(task, 1);

        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        verify(userService).findById(1);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void createTaskWithUser_UserNotFound() {
        when(userService.findById(any(Integer.class))).thenThrow(new EntityNotFoundException());

        assertThrows(EntityNotFoundException.class, () -> {
            taskService.createTaskWithUser(task, 2);
        });

        verify(userService).findById(2);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void createTaskWithoutUser_Success() {
        Task taskWithoutUser = new Task();
        taskWithoutUser.setPriority(Priority.MEDIUM);

        when(taskRepository.save(any(Task.class))).thenReturn(taskWithoutUser);

        Task result = taskService.createTaskWithoutUser(taskWithoutUser);

        assertNotNull(result);
        assertNull(result.getUser());
        assertNotNull(result.getCreatedAt());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void findById_InvalidId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.findById(0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.findById(-5);
        });

        verify(taskRepository, never()).findById(any(Integer.class));
    }

    @Test
    void findById_Success() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        Task result = taskService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(taskRepository).findById(1);
    }

    @Test
    void findById_TaskNotFound_ThrowsException() {
        when(taskRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            taskService.findById(99);
        });

        verify(taskRepository).findById(99);
    }


    @Test
    void findByUserId_Success() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        Task task2 = new Task();
        task2.setId(2);
        tasks.add(task2);

        when(taskRepository.findAllByUserId(1)).thenReturn(tasks);

        List<Task> result = taskService.findByUserId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(taskRepository).findAllByUserId(1);
    }

    @Test
    void findByUserId_EmptyList_ThrowsException() {
        when(taskRepository.findAllByUserId(1)).thenReturn(new ArrayList<>());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.findByUserId(1);
        });

        assertEquals("По этому пользователю нет задач", exception.getMessage());
        verify(taskRepository).findAllByUserId(1);
    }

    @Test
    void findByPriority_Success() {
        List<Task> highPriorityTasks = new ArrayList<>();
        highPriorityTasks.add(task);
        Task taskHigh = new Task();
        taskHigh.setPriority(Priority.HIGH);
        highPriorityTasks.add(taskHigh);

        when(taskRepository.findAllByPriority("Высокий")).thenReturn(highPriorityTasks);

        List<Task> result = taskService.findByPriority(Priority.HIGH);

        assertEquals(2, result.size());
        verify(taskRepository).findAllByPriority("Высокий");
    }

    @Test
    void findByPriority_EmptyResult_ReturnsEmptyList() {
        when(taskRepository.findAllByPriority("Низкий")).thenReturn(new ArrayList<>());

        List<Task> result = taskService.findByPriority(Priority.LOW);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(taskRepository).findAllByPriority("Низкий");
    }

    @Test
    void getAll_Success() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(new Task());

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAll();

        assertEquals(2, result.size());
        verify(taskRepository).findAll();
    }

    @Test
    void deleteTask_Success() {
        doNothing().when(taskRepository).delete(any(Task.class));

        boolean result = taskService.deleteTask(task);

        assertTrue(result);
        verify(taskRepository).delete(task);
    }
}