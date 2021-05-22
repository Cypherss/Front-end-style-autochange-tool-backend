package com.example.matchreplace.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InternalNode extends TreeNode {
    private List<TreeNode> children;

    public InternalNode(double areaPercent, Size size, String type, double relativeToPageTop, double relativeToPageCenter) {
        super(areaPercent,size,type,relativeToPageTop,relativeToPageCenter);
    }

    public InternalNode(Info info, ArrayList<TreeNode> childNodes, double areaPercent, Size size, String type, double relativeToPageTop, double relativeToPageCenter) {
        super(info,areaPercent,size,type,relativeToPageTop,relativeToPageCenter);
        this.children = childNodes;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
    public int getChildrenNum(){
        return children.size();
    }

    /**
     *
     * @return 返回自己，儿子，孙子的个数
     */
    public int getLocalNodeNum(){
        int res = children.size();
        for(int i =0; i<children.size();i++){
            if(children.get(i).getClass()==InternalNode.class)
                res += ((InternalNode)children.get(i)).getChildrenNum();
            else
                res += 1;
        }
        return res + 1;
    }



    public HashMap<String, Integer> getTagMap(){
        HashMap<String, Integer> map = new HashMap<>();
        for (TreeNode node: children) {
            int count = map.getOrDefault(node.getType(), 0) + 1;
            map.put(node.getType(), count);
            if(node.getClass()==InternalNode.class) {
                for (TreeNode subNode : ((InternalNode) node).children) {
                    count = map.getOrDefault(subNode.getType(), 0) + 1;
                    map.put(subNode.getType(), count);
                }
            }
        }
        return map;

    }



}
