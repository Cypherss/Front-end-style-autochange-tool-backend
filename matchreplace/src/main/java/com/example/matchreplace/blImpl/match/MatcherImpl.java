package com.example.matchreplace.blImpl.match;

import com.example.matchreplace.MatchReplaceApplication;
import com.example.matchreplace.bl.match.Matcher;
import com.example.matchreplace.bl.preprocess.Preprocessor;
import com.example.matchreplace.global.GlobalVatiables;
import com.example.matchreplace.vo.InternalNode;
import com.example.matchreplace.vo.LeafNode;
import com.example.matchreplace.vo.Size;
import com.example.matchreplace.vo.TreeNode;
import com.example.matchreplace.bl.match.Matcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatcherImpl implements Matcher {

    @Autowired
    Preprocessor preprocessor;

    public void matchControl(String fileId1, String fileId2){
        TreeNode body1 = preprocessor.toTree(fileId1);
        TreeNode body2 = preprocessor.toTree(fileId2);
        match(body1,body2);
    }
    /**
     * 匹配的总入口，对两棵DOM树进行匹配,主要将分完组的DOM节点传给nodeToNodeMatch进行匹配
     * @param root1 被替换页面DOM树
     * @param root2 替换页面DOM树
     */

    @Override
    public void match(TreeNode root1, TreeNode root2) {
        HashMap<String, HashMap<Size, List<TreeNode>>> nodeMap1 = treeToMap(root1);
        HashMap<String, HashMap<Size, List<TreeNode>>> nodeMap2 = treeToMap(root2);
        Set<String> keysOfMap1 = nodeMap1.keySet();
        Set<String> keysOfMap2 = nodeMap2.keySet();
        for(String key : keysOfMap1){
            if(keysOfMap2.contains(key)){
                Set<Size> sizeSet1 = nodeMap1.get(key).keySet();
                Set<Size> sizeSet2 = nodeMap2.get(key).keySet();
                for(Size size : sizeSet1) {
                    if (sizeSet2.contains(size))
                        nodeToNodeMatch(nodeMap1.get(key).get(size), nodeMap2.get(key).get(size));
                }
            }
        }
        GlobalVatiables.matchedBody = root1;
    }

    /**
     * 辅助方法，转化为字典，减少下面的计算量
     * @param root DOM树根节点
     * @return 返回一个已经通过Type和Size进行分类的Map
     */
    @Override
    public HashMap<String, HashMap<Size, List<TreeNode>>> treeToMap(TreeNode root) {
        HashMap<String, HashMap<Size, List<TreeNode>>> nodeMap = new HashMap<>();
        /*
        * 广度优先遍历*/
        Queue<TreeNode> nodes = new LinkedList<>();
        nodes.offer(root);
        while(!nodes.isEmpty()){
            TreeNode now = nodes.poll();
            if(!nodeMap.containsKey(now.getType())){
                HashMap<Size, List<TreeNode>> sameTypeNodeMap = new HashMap<>();
                List<TreeNode> sameSizeNodes = new ArrayList<TreeNode>(){{
                    add(now);
                }};
                sameTypeNodeMap.put(now.getSize(), sameSizeNodes);
                nodeMap.put(now.getType(), sameTypeNodeMap);
            }else{
                if(!nodeMap.get(now.getType()).containsKey(now.getSize())){
                    List<TreeNode> sameSizeNodes = new ArrayList<TreeNode>(){{
                        add(now);
                    }};
                    nodeMap.get(now.getType()).put(now.getSize(), sameSizeNodes);
                }else{
                    nodeMap.get(now.getType()).get(now.getSize()).add(now);
                }
            }
            if(now.getType().equals("div")) {
                for (TreeNode child : ((InternalNode)now).getChildren()) {
                    nodes.offer(child);
                }
            }
        }
        return nodeMap;
    }

    /**
     * 进行节点到节点的匹配
     * n1 n2 n3 n4 n5 n6 n7 n8 n9
     *                   |
     *                 validIndex，表示后面的节点都没有匹配的对应节点，意为垃圾节点
     * @param nodes1 被替换页面的部分同Type，同Size的节点
     * @param nodes2 替换页面的部分同Type，同Size的节点
     * @return 返回有匹配项节点的个数
     */
    @Override
    public int nodeToNodeMatch(List<TreeNode> nodes1, List<TreeNode> nodes2) {
        int size1 = nodes1.size();
        int size2 = nodes2.size();
        double[][] similarityArray = new double[size1][size2];
        double maxDifference = 0;
        for(int i = 0; i<size1; i++){
            for(int j =0; j<size2; j++){
                double difference = getDifference(nodes1.get(i), nodes2.get(j));
                similarityArray[i][j] = difference;
                maxDifference = Math.max(difference,maxDifference);
            }
        }
        double maxSimilarity = 0;
        for(int i = 0; i<size1; i++) {
            for (int j = 0; j < size2; j++) {
                similarityArray[i][j] = (maxDifference - similarityArray[i][j]) / maxDifference;
                maxSimilarity = Math.max(maxSimilarity, similarityArray[i][j]);
            }
        }

        int[] rowRemained = new int[size1];//为0表示还可以用，为1表示已经被使用
        int rowRemainedNum = size1;
        int[] colRemained = new int[size2];
        int colRemainedNum = size2;
        double nowMaxSimilarity = 0;
        int maxRow = 0;
        int maxCol = 0;
        while(rowRemainedNum!=0&&colRemainedNum!=0){
            nowMaxSimilarity = 0;
            for(int i =0; i< size1; i++){
                if(rowRemained[i] == 1)
                    continue;
                for(int j = 0; j<size2; j++){
                    if(colRemained[j] == 1)
                        continue;
                    if(similarityArray[i][j] > nowMaxSimilarity){
                        nowMaxSimilarity = similarityArray[i][j];
                        maxRow = i;
                        maxCol = j;
                    }
                }
            }
            //相似度过低，就跳出循环，不进行匹配
            if(nowMaxSimilarity<0.3*maxSimilarity)
                break;
            //已经确定最大的similarity的位置，首先进行匹配，接着将这行和这列变为不可使用，similarity设为0，进入下一轮
            nodes1.get(maxRow).setMatcher(nodes2.get(maxCol));
            nodes2.get(maxCol).setMatcher(nodes1.get(maxRow));
            rowRemained[maxRow] = 1;
            rowRemainedNum -= 1;
            colRemained[maxCol] = 1;
            colRemainedNum -= 1;

        }

        return 0;
    }

    /**
     *通过
     * relativeToPageTop 和 relativeToPageCenter 确定位置 areaPercent 面积占比 一起使用欧氏距离
     * 自己Type的类型
     * 对于内部节点还要看 children 的个数以及Type类型
     * 计算局部区域内的Tag组成的概率分布的相对熵和作为r
     * 成为r的矩阵，归一化
     * 每次取最大的，然后这行和这列清0，建立映射关系
     * 继续找最大的
     * @return 两个节点相似度
     */

    @Override
    public double getDifference(TreeNode node1, TreeNode node2) {
        double[] positionVector1 = {node1.getRelativeToPageTop(), node1.getrelativeToPageCenter(), node1.getAreaPercent()};
        double[] positionVector2 = {node2.getRelativeToPageTop(), node2.getrelativeToPageCenter(), node2.getAreaPercent()};
        double od = getODistance(positionVector1,positionVector2);
        double lsd = getLocalStructureDifference(node1, node2);
        return od + lsd;
    }

    /**
     * 计算欧几里得距离
     * @param vectorA
     * @param vectorB
     * @return 0-1 double
     */
    private double getODistance(double[] vectorA, double[] vectorB) {
        double distance = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            distance += Math.pow(vectorA[i]-vectorB[i], 2);
        }
        return Math.sqrt(distance);
    }

    private double getLocalStructureDifference(TreeNode node1, TreeNode node2){
        if(!node1.getType().equals(node2.getType()))
            return 0;
        else if(node1.getClass() == LeafNode.class)
            return 1;
        //否则进行局部相似度计算
        HashMap<String, Integer> tagMap1 = ((InternalNode)node1).getTagMap();
        HashMap<String, Integer> tagMap2 = ((InternalNode)node2).getTagMap();
        Set<String> allTag = new HashSet<>();
        allTag.addAll(tagMap2.keySet());
        allTag.addAll(tagMap1.keySet());
        double[] p1 = new double[allTag.size()];
        double[] p2 = new double[allTag.size()];
        int index = 0;
        for(String tag:allTag){
            p1[index] = tagMap1.getOrDefault(tag, 0);
            p2[index] = tagMap2.getOrDefault(tag, 0);
            index++;
        }
        normalized(p1);
        normalized(p2);
        return relativeEntropy(p1, p2) + relativeEntropy(p2, p1);
    }

    private void normalized(double[] list){
        int sum = 0;
        for (double value: list) {
            sum += value;
        }
        for(int i =0;i<list.length;i++){
            list[i] = list[i]/sum + 0.01;
        }
    }

    private double relativeEntropy(double[] P, double[] Q){
        double re = 0;
        for(int i = 0;i<P.length;i++){
            re += P[i]* Math.log(P[i]/Q[i]);
        }
        return re;
    }
}
