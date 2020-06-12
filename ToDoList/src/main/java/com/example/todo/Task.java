package com.example.todo;

import java.util.Calendar;

public class Task {

    private int id;
    private String name;
    private String description;
    private Priority priority;
    private Calendar deadline;

    public Task(int id, String name, String description, Priority priority, Calendar deadline) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
    }

    public enum Priority {
        VERYLOW, LOW, MEDIUM, HIGH, VERYHIGH
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }
}