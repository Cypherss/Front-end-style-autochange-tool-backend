package com.example.demo.DTO;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-05-16 21:02
 */
public class RecordDTO {
    int userId;
    String sourceId;
    String targetId;
    String time;
    String sourceName;
    String targetName;

    public RecordDTO(){}

    public RecordDTO(int userId, String sourceId, String targetId, String time, String sourceName, String targetName) {
        this.userId = userId;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.time = time;
        this.sourceName = sourceName;
        this.targetName = targetName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
}
