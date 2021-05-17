package com.example.zuul.DTO;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-05-17 19:57
 */
public class SourceFileWithCount {
    String id;
    String name;
    String uploadTime;
    String type;
    int creatorId;
    int num;

    public SourceFileWithCount(){}

    public SourceFileWithCount(SourceFile sourceFile){
        this.id = sourceFile.getId();
        this.name = sourceFile.getName();
        this.uploadTime = sourceFile.getUploadTime();
        this.type = sourceFile.getType();
        this.creatorId = sourceFile.getCreatorId();
        this.num = 0;
    }

    public SourceFileWithCount(String id, String name, String uploadTime, String type, int creatorId, int num) {
        this.id = id;
        this.name = name;
        this.uploadTime = uploadTime;
        this.type = type;
        this.creatorId = creatorId;
        this.num = num;
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
