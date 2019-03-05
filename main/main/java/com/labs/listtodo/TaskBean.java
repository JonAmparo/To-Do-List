package com.labs.listtodo;

public class TaskBean {
    private String title;
    private String description;
    private String date;
    private int priority;
    private boolean active;
    private int id;


    public TaskBean(String title, String description, String date, int priority, boolean active) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.priority = priority;
        this.active = active;
    }

    public TaskBean(int id, String title, String description, String date, int priority, boolean active) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.priority = priority;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


}