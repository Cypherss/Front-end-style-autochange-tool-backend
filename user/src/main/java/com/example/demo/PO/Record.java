package com.example.demo.PO;

import com.example.demo.DTO.RecordDTO;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-05-16 20:50
 */
public class Record {
    int userId;
    String sourceId;
    String targetId;
    String time;

    public Record(){};

    public Record(int userId, String sourceId, String targetId, String time) {
        this.userId = userId;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.time = time;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUserId() {
        return userId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString(){
        return "{"+this.userId+", "+this.sourceId+", "+this.targetId+", "+this.time+"}";
    }

    public RecordDTO getDTO(){
        String[] temp = this.targetId.split("-");
        String targetName = temp[0];
        String sourceName = "";
        return new RecordDTO(this.userId,this.sourceId,this.targetId,this.time,sourceName,targetName);
    }
}
