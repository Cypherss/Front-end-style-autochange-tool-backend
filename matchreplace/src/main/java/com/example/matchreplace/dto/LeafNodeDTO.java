package com.example.matchreplace.dto;


import com.example.matchreplace.vo.Info;
import com.example.matchreplace.vo.LeafNode;

public class LeafNodeDTO extends TreeNodeDTO {
    String content;
    String type;
    public LeafNodeDTO(Info info, String content, String type){
        super(info);
        this.content = content;
        this.type = type;
    }
    public LeafNodeDTO(LeafNode leafNode){
        super(leafNode.getInfo());
        this.type = leafNode.getType();
        this.content = leafNode.getContent();
    }
}
