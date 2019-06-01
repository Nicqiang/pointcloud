package com.github.nicqiang.pointcloud.utils;

import com.github.nicqiang.pointcloud.algorithm.tree.DataNode;
import com.github.nicqiang.pointcloud.algorithm.tree.KdTree;
import com.github.nicqiang.pointcloud.domain.PointCloud;

import java.util.ArrayList;
import java.util.List;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-06-01
 */
public class KdTreeUtil {

    /**
     * point cloud to kd tree
     * @param pointCloud
     * @return
     */
    public static KdTree pointCloudToKdTree(PointCloud pointCloud){
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
        return new KdTree(list);
    }
}
