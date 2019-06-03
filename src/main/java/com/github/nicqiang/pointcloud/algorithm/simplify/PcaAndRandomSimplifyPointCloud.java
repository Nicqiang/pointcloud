package com.github.nicqiang.pointcloud.algorithm.simplify;

import com.github.nicqiang.pointcloud.algorithm.tree.DataNode;
import com.github.nicqiang.pointcloud.domain.PointCloud;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-06-02
 */
@Slf4j
public class PcaAndRandomSimplifyPointCloud {

    private static final Random random = new Random(System.currentTimeMillis());

    public static void simplify(PointCloud pointCloud, int k, float eps, float factory){

        long startTime = System.currentTimeMillis();

        List<DataNode> nodeList = PcaSimplifyPointCloud.markFeaturePoint(pointCloud, k, eps);
        nodeList.stream().filter(node -> !node.isFeature()).forEach(dataNode -> {
            if(random.nextDouble() < factory){
                dataNode.setDelete(true);
            }
        });

        //移除可删除点
        StringBuilder sb = new StringBuilder();

        pointCloud.setPointNum(0L);

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
