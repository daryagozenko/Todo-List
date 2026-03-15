package com.gozenko.TodoList.DTO;

import com.gozenko.TodoList.models.Task;
import com.gozenko.TodoList.utils.Priority;

import java.time.LocalDate;

public class TaskRequest {
    private String naming;
    private String priority;
    private Integer userId;

    public TaskRequest() {
    }

    public TaskRequest(String naming, String priority, Integer userId) {
        this.naming = naming;
        this.priority = priority;
        this.userId = userId;
    }

    public TaskRequest(String naming, String priority) {
        this.naming = naming;
        this.priority = priority;
    }

    public String getNaming() {
        return naming;
    }

    public void setNaming(String naming) {
        this.naming = naming;
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
}
