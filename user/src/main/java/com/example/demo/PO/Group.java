package com.example.demo.PO;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-04-25 17:15
 */
public class Group {
    int id;
    String name;
    String description;

    public Group(){}

    public Group(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Group(String name, String description) {
        this.name = name;
        this.description = description;
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
}
