package com.github.nicqiang.pointcloud.algorithm.simplify;

import com.github.nicqiang.pointcloud.algorithm.matrix.MatrixComputeUtil;
import com.github.nicqiang.pointcloud.algorithm.tree.DataNode;
import com.github.nicqiang.pointcloud.algorithm.tree.KdTree;
import com.github.nicqiang.pointcloud.domain.PointCloud;
import com.github.nicqiang.pointcloud.utils.KdTreeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-06-02
 */
@Slf4j
public class PcaSimplifyPointCloud {

    private static final double MIN_EPS = 1.0E-6;

    public static void simplify(PointCloud pointCloud, int k, float eps){

        long startTime = System.currentTimeMillis();


        List<DataNode> nodeList = markFeaturePoint(pointCloud, k, eps);



        //移除所有非特征点
        StringBuilder sb = new StringBuilder();

        pointCloud.setPointNum(0L);

        nodeList.stream().filter(node -> node.isFeature()).forEach(dataNode -> {
            float[] value = dataNode.getValue();
            for (float v : value) {
                sb.append(v);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append(";");
            pointCloud.setPointNum(pointCloud.getPointNum() + 1);
        });
        pointCloud.setPoints(sb.toString());

        log.info("cost={}", System.currentTimeMillis() - startTime);

    }


    public static List<DataNode> markFeaturePoint(PointCloud pointCloud, int k, float eps){
        KdTree kdTree = KdTreeUtil.pointCloudToKdTree(pointCloud);
        List<DataNode> nodeList = kdTree.getNoeList();

        nodeList.forEach(dataNode -> {
            List<DataNode> kNearestDataNode = kdTree.getKNearestDataNode(dataNode, k);
            float[][] points = new float[kNearestDataNode.size()][3];
            for (int i = 0; i < points.length; i++) {
                points[i] = kNearestDataNode.get(i).getValue();
            }
            List<Double> eigen = MatrixComputeUtil.eigen(dataNode.getValue(), points);
            double realWp = computeWp(eigen);
            if(realWp > eps){
                dataNode.setFeature(true);
            }
            if(realWp < MIN_EPS){
                dataNode.setDelete(true);
            }
        });
        return nodeList;
    }

    /**
     * 计算wp
     * @param eigen
     * @return
     */
    private static double computeWp(List<Double> eigen){
        return eigen.get(0)/(eigen.get(0) + eigen.get(1) + eigen.get(2));
    }


}
