package com.example.matchreplace.vo;

public class LeafNode extends TreeNode{
    private String content;

    public LeafNode(double areaPercent, Size size, String type, double relativeToPageTop, double relativeToPageCenter) {
        super(areaPercent,size,type,relativeToPageTop,relativeToPageCenter);
    }

    public LeafNode(Info info, double areaPercent, Size size, String type, double relativeToPageTop, double relativeToPageCenter, String content) {
        super(info, areaPercent, size, type, relativeToPageTop, relativeToPageCenter);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
