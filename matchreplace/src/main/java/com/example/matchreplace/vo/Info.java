package com.example.matchreplace.vo;

public class Info {

    private int offsetWidth;

    private int offsetHeight;

    private int scrollWidth;

    private int scrollHeight;

    private int offsetLeft;

    private int offsetTop;

    private String tag;

    private String usedCss;

    private Object css;




    public double[] nodeDigitize(){
        return null;
    }
    public double[][] rootDigitize(){
        return null;
    }
    public double calSimilarity(Info node){
        return 0.0;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getOffsetWidth() {
        return offsetWidth;
    }

    public int getOffsetHeight() {
        return offsetHeight;
    }


    public int getScrollWidth() {
        return scrollWidth;
    }


    public int getScrollHeight() {
        return scrollHeight;
    }


    public int getOffsetLeft() {
        return offsetLeft;
    }

    public int getOffsetTop() {
        return offsetTop;
    }

    public String getUsedCss() {
        return usedCss;
    }

    public void setUsedCss(String usedCss) {
        this.usedCss = usedCss;
    }

    public Object getCss() {
        return css;
    }

    public void setCss(Object css) {
        this.css = css;
    }
}
