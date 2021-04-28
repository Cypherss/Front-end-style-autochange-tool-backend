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

    public Group(int id, String groupName, String description) {
        this.id = id;
        this.name = groupName;
        this.description = description;
    }

    public Group(String groupName, String description) {
        this.name = groupName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return name;
    }

    public void setGroupName(String groupName) {
        this.name = groupName;
    }

    public String getDesction() {
        return description;
    }

    public void setDesction(String desction) {
        this.description = desction;
    }
}
