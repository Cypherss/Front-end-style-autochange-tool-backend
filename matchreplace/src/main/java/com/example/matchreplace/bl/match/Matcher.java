package com.example.matchreplace.bl.match;

import com.example.matchreplace.vo.Size;
import com.example.matchreplace.vo.TreeNode;

import java.util.HashMap;
import java.util.List;

public interface Matcher {
    public void matchControl(String fileId1, String fileId2);
    public void match(TreeNode root1, TreeNode root2);
    public HashMap<String, HashMap<Size, List<TreeNode>>> treeToMap(TreeNode root);
    public double getDifference(TreeNode node1, TreeNode node2);
    public int nodeToNodeMatch(List<TreeNode> nodes1, List<TreeNode> nodes2);
}
