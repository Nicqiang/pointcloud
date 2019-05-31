package com.github.nicqiang.pointcloud.algorithm.dbscan;

import com.github.nicqiang.pointcloud.algorithm.tree.DataNode;
import com.github.nicqiang.pointcloud.algorithm.tree.KdTree;
import com.github.nicqiang.pointcloud.domain.PointCloud;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-05-31
 */
@Slf4j
public class DbScan {

    public static PointCloud removeNoise(PointCloud pointCloud){
        long startTime = System.currentTimeMillis();
        List<DataNode> list = new ArrayList<>();
        String[] pointStr = pointCloud.getPoints().split(";");
        for (String s : pointStr) {
            String[] v = s.split(",");
            float[] vf = new float[3];
            for (int i = 0; i < v.length; i++) {
                vf[i] = Float.valueOf(v[i]);
            }
            list.add(new DataNode(vf));
        }
        KdTree kdTree = new KdTree(list);
        //设置阀值
        int minPts = 5;
        float eps = 0.5f;

        //标记为核心点
        for (DataNode dataNode : list) {
            if(dataNode.getScan() == 0){
                List<DataNode> kNearestDataNode = kdTree.getKNearestDataNode(dataNode, minPts);
                if (kNearestDataNode != null && kNearestDataNode.size() == minPts){
                    boolean flag = true;
                    for (DataNode node : kNearestDataNode) {
                        double dist = KdTree.computeDistance(dataNode, node);
                        if (dist >= eps){
                            flag = false;
                            break;
                        }
                    }
                    if (flag){
                        dataNode.setScan(1);
                    }
                }
            }
        }

        //标记外点
        list.stream().filter(dataNode -> dataNode.getScan() == 1).forEach(dataNode -> {
            List<DataNode> kNearestDataNode = kdTree.getKNearestDataNode(dataNode, minPts);
            if (kNearestDataNode != null && kNearestDataNode.size() > 0){
                kNearestDataNode.stream().filter(node -> node.getScan() == 0).forEach(node -> {
                    double dist = KdTree.computeDistance(dataNode, node);
                    if (dist > eps) {
                        node.setScan(2);
                    }
                });
            }
        });

        //移除外点
        StringBuilder sb = new StringBuilder();
        list.stream().filter(dataNode -> dataNode.getScan() != 0).forEach(dataNode -> {
            float[] value = dataNode.getValue();
            for (float v : value) {
                sb.append(v);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append(";");
        });
        sb.deleteCharAt(sb.length()-1);
        pointCloud.setPoints(sb.toString());
        log.info("actionName=removeNoise||cost={}",System.currentTimeMillis() - startTime);
        return pointCloud;
    }
}
