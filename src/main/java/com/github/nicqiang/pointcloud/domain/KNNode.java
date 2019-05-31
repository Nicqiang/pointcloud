package com.github.nicqiang.pointcloud.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by
 *
 * @Author: nicqiang
 * @DATE: 2019/3/25
 */
@AllArgsConstructor
@Data
public class KNNode {

    /**
     * 距离
     */
    private double distance;

    private Point point;

}
