package com.example.matchreplace.dto;


import com.example.matchreplace.vo.Info;

public class InternalNodeDTO extends TreeNodeDTO {
    private TreeNodeDTO[] children;
    public InternalNodeDTO(Info info){
        super(info);
    }
    public InternalNodeDTO(Info info, TreeNodeDTO[] children){
        super(info);
        this.children = children;
    }

    public void setChildren(TreeNodeDTO[] children){
        this.children = children;
    }
}
