package com.github.nicqiang.pointcloud.algorithm.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.*;

/**
 * Created by
 * kd treed 封装
 * @Author: nicqiang
 * @DATE: 2019/3/28
 */
public class KdTree {

    /**
     * 其实节点
     */
    @Getter
    private KdNode kdNode;

    private int kdNodeNumber;

    @Getter
    private List<DataNode> noeList;

    /**
     * 点的维度
     */
    private int nodeDemen;

    private static final Random random = new Random(System.currentTimeMillis());

    /**
     * 初始化kd 树
     * @param nodeList {@link DataNode}
     */
    public KdTree(List<DataNode> nodeList) {

        this.noeList = nodeList;

        KdNode kdNode = new KdNode();
        nodeDemen = nodeList.get(0).getValue().length;
        kdNode.kDimensional = -1;
        createKdTree(kdNode,nodeList, 0);
        this.kdNode = kdNode;
        this.kdNodeNumber = nodeList.size();
    }


    /**
     * 获取 最近这个点
     * @param node node
     * @param k 最近k个点
     * @return
     */
    public List<DataNode> getKNearestDataNode(DataNode node,int k){
        if(this.kdNode == null){
            return null;
        }

        if(this.kdNodeNumber <= k){
            return getKdTreeAllDataNode();
        }
        Stack<KdNode> searchPath = new Stack<>();

        KNearestNode kNearestNode = new KNearestNode(node,k);

        buildSearchPath(kNearestNode,searchPath,this.kdNode);

        do{
            KdNode currentKdNode = searchPath.pop();
            if(kNearestNode.nearestKdNodes.size() < kNearestNode.k){
                kNearestNode.addDataNode(currentKdNode.getNode());
            }else {
                double dist = kNearestNode.computeDistance(currentKdNode.node);
                if(dist < kNearestNode.maxLength){
                    kNearestNode.replaceMaxDataNode(currentKdNode.node);
                }
            }

            int v = currentKdNode.getKDimensional();
            float disK = Math.abs(currentKdNode.node.getValue()[v] - kNearestNode.sourceNode.getValue()[v]);
            if(disK < kNearestNode.maxLength){
                //访问另外一个分支
                if(currentKdNode.node.getValue()[v] - kNearestNode.sourceNode.getValue()[v] >= 0){
                    //走right
                    if(currentKdNode.rightNode != null){
                        buildSearchPath(kNearestNode, searchPath, currentKdNode.getRightNode());
                    }
                }else {
                    //走left
                    if(currentKdNode.leftNode != null){
                        buildSearchPath(kNearestNode, searchPath, currentKdNode.getLeftNode());
                    }
                }
            }

        }while (!searchPath.empty());

        return kNearestNode.nearestKdNodes;


    }

    /**
     * 构建搜索路径，使用堆栈保存
     * @param kNearestNode
     * @param searchPath
     * @param kdNode
     */
    private void buildSearchPath(KNearestNode kNearestNode, Stack<KdNode> searchPath, KdNode kdNode) {
        DataNode sourceNode = kNearestNode.sourceNode;
        while (kdNode != null){
            searchPath.push(kdNode);
            int k = kdNode.kDimensional;
            if(sourceNode.getValue()[k] <= kdNode.node.getValue()[k]){
                kdNode = kdNode.getLeftNode();
            }else {
                kdNode = kdNode.getRightNode();
            }
        }
    }

    /**
     * 获取整棵树的所有节点
     * @return data nodes
     */
    private List<DataNode> getKdTreeAllDataNode() {
        List<DataNode> dataNodes = new ArrayList<>();

        //todo, 树的遍历
        return dataNodes;
    }

    /**
     * 计算两点之间的距离
     * @param node1 {@link DataNode}
     * @param node2 {@link DataNode}
     * @return dist
     */
    public static double computeDistance(DataNode node1, DataNode node2) {
        int len = node1.getValue().length;
        double dist = 0;
        for (int i = 0; i < len; i++) {
            dist += Math.pow(node1.getValue()[i] - node2.getValue()[i],2);
        }
        return Math.sqrt(dist);
    }


    /**
     * 创建一颗树
     * @param kdNode kn node
     * @param dataNodes data list
     * @param flag 0，1: left, 2:right
     *
     */
    private void createKdTree(KdNode kdNode, List<DataNode> dataNodes, int flag){
        int kd = (kdNode.kDimensional +1 )% this.nodeDemen;

        if(dataNodes.size() >= 1 && flag != 0){
            if (flag == 1){
                KdNode leftNode = new KdNode();
                leftNode.kDimensional = kd;
                kdNode.leftNode = leftNode;
                kdNode = leftNode;
            }else if (flag == 2){
                KdNode rightNode = new KdNode();
                rightNode.kDimensional = kd;
                kdNode.rightNode = rightNode;
                kdNode = rightNode;
            }
            if(dataNodes.size() == 1){
                kdNode.node = dataNodes.get(0);
                return;
            }
        }

        DivideInfo divideInfo = getDivideInfo(dataNodes, kd);
        kdNode.node = divideInfo.dataNode;
        kdNode.kDimensional = divideInfo.getKDimen();

        List<DataNode> leftDataNodes = new ArrayList<>();
        List<DataNode> rightDataNodes = new ArrayList<>();
        final float kValue = divideInfo.dataNode.getValue()[divideInfo.kDimen];
        final int k = divideInfo.kDimen;
        dataNodes.stream().filter(dataNode -> !dataNode.isUsed()).forEach(dataNode -> {
            if(dataNode.getValue()[k] <= kValue){
                leftDataNodes.add(dataNode);
            }else {
                rightDataNodes.add(dataNode);
            }
        });


        if(leftDataNodes.size() != 0){
            createKdTree(kdNode,leftDataNodes, 1);
        }

        if(rightDataNodes.size() != 0){
            createKdTree(kdNode,rightDataNodes, 2);
        }

    }


    /**
     * 获取划分点问题
     * @param dataNodes nodes
     * @return
     */
    private DivideInfo getDivideInfo(List<DataNode> dataNodes, int k){

        //使用平均距离进行
        double avg = dataNodes.stream().mapToDouble(node -> node.getValue()[k]).average().getAsDouble();
        DataNode node = findMidPointByAvgNum(dataNodes, k, avg);
        node.setUsed(true);
        return new DivideInfo(k, node);

        /*Double[] variances = new Double[k];
        for (int i = 0; i < k; i++) {
            variances[i] = getVariance(dataNodes,i);
        }

        int maxVarIndex = 0;
        double maxVar = 0;
        for (int i = 0; i < variances.length; i++) {
            if(variances[i] > maxVar){
                maxVar = variances[i];
                maxVarIndex = i;
            }
        }

        DataNode node = findMidPoint(dataNodes,maxVarIndex);
        node.setUsed(true);

        return new DivideInfo(maxVarIndex,node);*/
    }

    /**
     * 离平均值最近
     * @param dataNodes
     * @param  kd
     * @param avg
     * @return
     */
    private DataNode findMidPointByAvgNum(List<DataNode> dataNodes, int kd, double avg) {
        DataNode node = null;
        Double dmin = Double.MAX_VALUE;
        for (DataNode dataNode : dataNodes) {
            double dist = Math.abs(dataNode.getValue()[kd] - avg);
            if (dmin > dist){
                dmin = dist;
                node = dataNode;
            }
        }
        return node;
    }

    /**
     * 按某个维度找中值，并且将中值那个点找到，如果有多个，随机选一个。
     * @param dataNodes nodes
     * @param kDemen k
     * @return
     */
    public static DataNode findMidPoint(List<DataNode> dataNodes, int kDemen) {
        if(dataNodes.size() == 1){
            return dataNodes.get(0);
        }
        return quickFindMedianNode(dataNodes,kDemen,dataNodes.size()/2 + 1);

    }

    /**
     * 快速查找中间
     * @param dataNodes
     * @param kDemen
     * @param index
     * @return
     */
    private static DataNode quickFindMedianNode(List<DataNode> dataNodes, int kDemen, int index) {
        if(dataNodes.size() == 1){
            return dataNodes.get(0);
        }

        List<DataNode> left = new LinkedList<>();
        List<DataNode> right = new LinkedList<>();

        int randomIndex = random.nextInt(dataNodes.size());

        final float kValue = dataNodes.get(randomIndex).getValue()[kDemen];


        dataNodes.forEach(dataNode -> {
            if(dataNode.getValue()[kDemen] <= kValue){
                left.add(dataNode);
            }else {
                right.add(dataNode);
            }
        });

        if(left.size() == index){
            return dataNodes.get(randomIndex);
        }

        if(left.size() > index){
            return quickFindMedianNode(left,kDemen,index);
        }else {
            return quickFindMedianNode(right,kDemen,index-left.size());
        }

    }


    /**
     * 求方差，特征值
     * @param dataNodes
     * @param k
     * @return
     */
    private double getVariance(List<DataNode> dataNodes, final int k) {
        double average = dataNodes.stream().mapToDouble(dataNode -> dataNode.getValue()[k]).average().getAsDouble();

        return dataNodes.stream().mapToDouble(dataNode -> Math.pow(dataNode.getValue()[k]-average,2)).sum()/dataNodes.size();
    }


    /**
     * 最近K个点的封装
     */
    class KNearestNode{

        /**
         * 离目标点最近的k个点
         */
        private List<DataNode> nearestKdNodes;

        /**
         * 将距离存起来，空间换时间
         */
        private Map<DataNode,Double> distMap;

        /**
         * 需要找k个最近的点
         */
        private int k;

        /**
         * 改点的最近距离
         */
        private DataNode sourceNode;

        /**
         * 离目标点最远距离
         */
        private double maxLength;

        /**
         * 离目标点最近距离
         */
        private double minLength;


        /**
         * 构建的时候初始化
         * @param sourceNode source ndoe
         */
        public KNearestNode(DataNode sourceNode,int k) {
            this.sourceNode = sourceNode;
            this.nearestKdNodes = new LinkedList<>();
            this.distMap = new HashMap<>();
            this.minLength = Double.MAX_VALUE;
            this.maxLength = Double.MIN_VALUE;
            this.k = k;
        }


        /**
         * 加入一个点,保持从小到大的顺序
         * @param dataNode
         */
        public void addDataNode(DataNode dataNode){
            double dist = computeDistance(dataNode);
            this.distMap.put(dataNode,dist);

            if(dist > this.maxLength){
                this.maxLength = dist;
            }
            if (dist < this.minLength){
                this.minLength = dist;
            }

            if(this.nearestKdNodes.size() == 0){
                nearestKdNodes.add(dataNode);
            }else {
                for (int i = nearestKdNodes.size()-1; i >= 0; i--) {
                    if(dist >= distMap.get(nearestKdNodes.get(i))){
                        if(i+1 == nearestKdNodes.size()){
                            nearestKdNodes.add(dataNode);
                        }else {
                            nearestKdNodes.add(i+1,dataNode);
                        }
                        break;
                    }
                    if(i == 0){
                        nearestKdNodes.add(i,dataNode);
                    }
                }
            }
        }


        /**
         * 计算两点之间的距离
         * @param node {@link DataNode}
         * @return dist
         */
        public double computeDistance(DataNode node) {
            int len = sourceNode.getValue().length;
            double dist = 0;
            for (int i = 0; i < len; i++) {
                dist += Math.pow(sourceNode.getValue()[i] - node.getValue()[i],2);
            }
            return Math.sqrt(dist);
        }

        /**
         * 替换最大的点
         * @param node
         */
        public void replaceMaxDataNode(DataNode node) {
            this.addDataNode(node);
            DataNode removeData = this.nearestKdNodes.remove(this.nearestKdNodes.size() - 1);
            distMap.remove(removeData);
            DataNode dataNode = this.nearestKdNodes.get(this.nearestKdNodes.size() - 1);
            Double dist = this.distMap.get(dataNode);
            this.maxLength = dist;
            if(this.minLength > dist){
                this.minLength = dist;
            }
        }
    }

    @Data
    @AllArgsConstructor
    class DivideInfo{

        /**
         * 维度
         */
        private int kDimen;

        /**
         * 划分点
         */
        private DataNode dataNode;



    }




    @Getter
    static class KdNode{
        /**
         * 当前节点
         */
        private DataNode node;

        /**
         * 评判数据的维度
         */
        private int kDimensional;

        /**
         * 左节点
         */
        private KdNode leftNode;

        /**
         * 右节点
         */
        private KdNode rightNode;
    }
}
