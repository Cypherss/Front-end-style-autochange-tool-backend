package com.example.matchreplace.vo;

public class TreeNode {
    private Info info;
    private String type;
    private double areaPercent = 0;
    private double relativeToPageTop;
    private double relativeToPageCenter;
    private Size size = null;
    private TreeNode matcher = null;

    public TreeNode(){}

    public TreeNode(double areaPercent, Size size, String type, double relativeToPageTop, double relativeToPageCenter) {
        this.areaPercent = areaPercent;
        this.size = size;
        this.type = type;
        this.relativeToPageCenter = relativeToPageCenter;
        this.relativeToPageTop = relativeToPageTop;
    }

    public TreeNode(Info info, double areaPercent, Size size, String type, double relativeToPageTop, double relativeToPageCenter) {
        this.info = info;
        this.areaPercent = areaPercent;
        this.size = size;
        this.type = type;
        this.relativeToPageCenter = relativeToPageCenter;
        this.relativeToPageTop = relativeToPageTop;
    }

    public TreeNode getMatcher() {
        return matcher;
    }

    public void setMatcher(TreeNode matcher) {
        this.matcher = matcher;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public double getAreaPercent() {
        return areaPercent;
    }

    public void setAreaPercent(int areaPercent) {
        this.areaPercent = areaPercent;
    }


    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRelativeToPageTop() {
        return relativeToPageTop;
    }

    public void setRelativeToPageTop(double relativeToPageTop) {
        this.relativeToPageTop = relativeToPageTop;
    }

    public double getrelativeToPageCenter() {
        return relativeToPageCenter;
    }

    public void setrelativeToPageCenter(double relativeToPageCenter) {
        this.relativeToPageCenter = relativeToPageCenter;
    }
}
