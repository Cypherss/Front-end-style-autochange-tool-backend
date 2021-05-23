package com.example.matchreplace.blImpl.preprocess;


import com.example.matchreplace.Utils.ObjectStorageUtils;
import com.example.matchreplace.bl.preprocess.Preprocessor;
import com.example.matchreplace.vo.*;
import com.google.gson.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PreprocessorImpl implements Preprocessor {

    @Autowired
    ObjectStorageUtils objectStorageUtils;

    private int pageWidth = 0;

    private int pageHeight = 0;

    private int pageArea = 0;
    @Override
    public TreeNode toTree(String fileId) {
        String json = objectStorageUtils.getFileContetnt(fileId);
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        if(!getPageInfo(jsonObject)) {
            System.out.println("页面长宽存在问题");
            return new TreeNode();
        }
        TreeNode root = toTreeHelp(jsonObject, 0, 0);
        return root;
    }

    /**
     *
     * @param jsonNode json节点
     * @return
     */
    private TreeNode toTreeHelp(JsonObject jsonNode, int absoluteToTopOfParent, int absoluteToLeftOfParent){
        Gson gson = new Gson();
        Info info = gson.fromJson(jsonNode.get("info").toString(), Info.class);
        Object css = info.getCss();
        String cssString = gson.toJson(css);
        if(cssString.contains("\"display\":\"none\"")){
            System.out.println(cssString);
            return null;
        }
        double areaPercent = getAreaPercentFromInfo(info);
        Size size = getSizeFromArea(areaPercent);
        int nowToTop = absoluteToTopOfParent + info.getOffsetTop();
        int nowToLeft = absoluteToLeftOfParent + info.getOffsetLeft();
        double relativeToPageTop = nowToTop/this.pageHeight;
        double relativeToPageCenter = Math.abs(0.5 - nowToLeft/this.pageWidth);
        JsonArray children = null;
        if(jsonNode.get("children")!=null) {
            if (jsonNode.get("children").isJsonArray())
                children = jsonNode.getAsJsonArray("children");
        }
        if(children!=null){
            ArrayList<TreeNode> childNodes = new ArrayList<>();
            InternalNode internalNode = new InternalNode(info,childNodes,areaPercent,size,"div",relativeToPageTop,relativeToPageCenter);
            for(int i = 0; i< children.size(); i++){
                if(children.get(i).isJsonObject()) {
                    JsonObject child = children.get(i).getAsJsonObject();
                    TreeNode tmp = toTreeHelp(child, nowToTop, nowToLeft);
                    if(tmp!=null)
                        childNodes.add(tmp);
                }
            }
            return internalNode;
        }
        else{
            String content = "";
            if(jsonNode.get("content")!=null) {
                content = jsonNode.get("content").toString();
                content = content.substring(1, content.length() - 1);
            }

            String type = jsonNode.get("type").toString();
            type = type.substring(1, type.length()-1);
            if(type.equals("null"));
            type = info.getTag();
            LeafNode leafNode = new LeafNode(info,areaPercent,size,type,relativeToPageTop,relativeToPageCenter,content);
            return leafNode;
        }
    }

    /**
     * 用于获取最大的Area，方便下面计算占比，同时设置页面长和宽
     * @param rootNode 一定要传根节点，因为这样才能是最大的Area
     * @return
     */
    private boolean getPageInfo(JsonObject rootNode){
        Gson gson = new Gson();
        Info info = gson.fromJson(rootNode.get("info").toString(), Info.class);
        this.pageHeight = info.getOffsetHeight();
        this.pageWidth = info.getOffsetWidth();
        if(this.pageWidth!=0&&this.pageHeight!=0) {
            this.pageArea = info.getOffsetHeight()*info.getOffsetWidth();
            return true;
        }
        return false;


    }

    private double getAreaPercentFromInfo(Info info){
        double areaPercent = info.getOffsetHeight()*info.getOffsetWidth()/pageArea;
        return areaPercent;
    }

    private Size getSizeFromArea(double area){
        Size size;
        double percent = area;
        if(percent>0.5&&percent<=1)
            size = Size.BIG;
        else if(percent>0.25&&percent<=0.5)
            size = Size.MIDDLE;
        else if(percent>0.125&&percent<=0.25)
            size = Size.SMALL;
        else
            size = Size.MICRO;
        return size;
    }

}
