package com.example.zuul.DTO;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-05-05 14:38
 */
public class SourceFile {
    String id;
    String name;
    String uploadTime;
    String type;
    int creatorId;

    public SourceFile(){}

    public SourceFile(String id, String name, String uploadTime, String type, int creatorId) {
        this.id = id;
        this.name = name;
        this.uploadTime = uploadTime;
        this.type = type;
        this.creatorId = creatorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }
}
