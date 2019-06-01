package com.github.nicqiang.pointcloud.algorithm.simplify;

import com.github.nicqiang.pointcloud.algorithm.tree.DataNode;
import com.github.nicqiang.pointcloud.algorithm.tree.KdTree;
import com.github.nicqiang.pointcloud.domain.PointCloud;
import com.github.nicqiang.pointcloud.utils.KdTreeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <h>基于给定距离的简化</h>
 *
 * @author nicqiang
 * @since 2019-06-01
 */
@Slf4j
public class MinDistSimplifyPointCloud {

    public static void simplify(PointCloud pointCloud,int k, float minDist){
        long startTime = System.currentTimeMillis();
        KdTree kdTree = KdTreeUtil.pointCloudToKdTree(pointCloud);
        List<DataNode> nodeList = kdTree.getNoeList();
        nodeList.stream().filter(node -> !node.isDelete()).forEach(dataNode -> {
            List<DataNode> kNearestDataNode = kdTree.getKNearestDataNode(dataNode, k);
            kNearestDataNode.stream()
                    .filter(kDateNode -> !kDateNode.isDelete() && KdTree.computeDistance(dataNode,kDateNode) < minDist)
                    .forEach(kdNode -> kdNode.setDelete(true));
        });

        StringBuilder sb = new StringBuilder();

        nodeList.stream().filter(node -> !node.isDelete()).forEach(dataNode -> {
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
}
