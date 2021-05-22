package com.example.matchreplace.dto;


import com.example.matchreplace.vo.Info;

public class TreeNodeDTO {
    private Info info;
    public TreeNodeDTO(Info info){
        this.info = info;
    }
    public Info getInfo(){
        return info;
    }
}
