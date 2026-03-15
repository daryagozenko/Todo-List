package com.gozenko.TodoList.services;

import com.gozenko.TodoList.models.Task;
import com.gozenko.TodoList.repositories.TaskRepository;
import com.gozenko.TodoList.utils.Priority;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public Task createTaskWithUser(Task task, Integer userId){
        task.setCreatedAt(LocalDate.now());
        task.setUser(userService.findById(userId));
        return taskRepository.save(task);
    }

    public Task createTaskWithoutUser(Task task){
        task.setCreatedAt(LocalDate.now());
        task.setUser(null);
        return taskRepository.save(task);
    }

    public Task findById(Integer id){
        if(id <= 0) throw new IllegalArgumentException("ID должно быть > 0");
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
    }

    public List<Task> findByUserId(Integer id){
        List<Task> list = taskRepository.findAllByUserId(id);
        if(list.isEmpty()) throw new IllegalArgumentException("По этому пользователю нет задач");
        return list;
    }

    public List<Task> findByPriority(Priority priority){
        return taskRepository.findAllByPriority(priority.getPriorityName());
    }

    public List<Task> getAll(){
        return taskRepository.findAll();
    }

    public boolean deleteTask(Task task){
        taskRepository.delete(task);
        return true;
    }
}
