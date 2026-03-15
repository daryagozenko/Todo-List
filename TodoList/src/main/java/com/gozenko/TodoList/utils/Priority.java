package com.gozenko.TodoList.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Priority {
    LOW("Низкий"),
    MEDIUM("Средний"),
    HIGH("Высокий");

    private final String priorityName;

    Priority(String naming) {
        this.priorityName = naming;
    }

    public String getPriorityName() {
        return priorityName;
    }

    @JsonValue
    public String getPriorityNameForJson() {
        return priorityName;
    }

    @JsonCreator
    public static Priority fromString(String value) {
        for (Priority priority : Priority.values()) {
            if (priority.priorityName.equals(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Неизвестный приоритет: " + value);
    }
}
