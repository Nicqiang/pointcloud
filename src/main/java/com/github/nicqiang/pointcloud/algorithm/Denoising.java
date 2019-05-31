package com.github.nicqiang.pointcloud.algorithm;

import com.github.nicqiang.pointcloud.algorithm.dbscan.DbScan;
import com.github.nicqiang.pointcloud.domain.PointCloud;

/**
 * Created by
 * 去噪算法
 * @Author: nicqiang
 * @DATE: 2019/3/17
 */
public class Denoising {

    /**
     * 采用dbscan去噪
     * @param pointCloud
     * @return
     */
    public static PointCloud dbScanReoveNoise(PointCloud pointCloud){
        return DbScan.removeNoise(pointCloud);
    }
}
