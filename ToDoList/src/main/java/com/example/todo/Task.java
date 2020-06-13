package com.example.todo;

import android.graphics.Color;

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
        VERYLOW(Color.argb(255,0,255,0)),
        LOW(Color.argb(255,180,255,0)),
        MEDIUM(Color.argb(255,255,255,0)),
        HIGH(Color.argb(255,255,180,0)),
        VERYHIGH(Color.argb(255,255,0,0));

        private final int value;

        Priority(final int newValue){
            value = newValue;
        }
        public int getValue(){
            return value;
        }
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