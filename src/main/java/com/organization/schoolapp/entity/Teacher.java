package com.organization.schoolapp.entity;

public class Teacher {
    private int id;
    private String name;

    public Teacher (int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Teacher (String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public void setName(String newName) {
        this.name = newName;
    }
}
