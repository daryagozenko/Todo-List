package com.gozenko.TodoList.DTO;

import com.gozenko.TodoList.models.Task;
import com.gozenko.TodoList.models.User;
import com.gozenko.TodoList.utils.Priority;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskResponse {

    private String naming;
    private LocalDate createdAt;
    private String priority;
    private Integer userId;

    public TaskResponse(Task task){
        this.naming = task.getNaming();
        this.createdAt = task.getCreatedAt();

        if (task.getPriority() != null) {
            this.priority = task.getPriority().getPriorityName();
        } else {
            this.priority = "Не указан";
        }

        if(task.getUser() == null) return;
        this.userId = task.getUser().getId();
    }

    public static List<TaskResponse> fromTasks(List<Task> tasks) {
        List<TaskResponse> responses = new ArrayList<>();
        for (Task task : tasks) {
            responses.add(new TaskResponse(task));
        }
        return responses;
    }

    public String getNaming() {
        return naming;
    }

    public void setNaming(String naming) {
        this.naming = naming;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    //    public UserResponse getUser() {
//        return user;
//    }
//
//    public void setUser(UserResponse user) {
//        this.user = user;
//    }
}
