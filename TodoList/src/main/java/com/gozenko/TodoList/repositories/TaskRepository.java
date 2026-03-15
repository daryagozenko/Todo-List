package com.gozenko.TodoList.repositories;

import com.gozenko.TodoList.models.Task;
import com.gozenko.TodoList.utils.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByUserId(Integer id);
    List<Task> findAllByPriority(String priority);
}
