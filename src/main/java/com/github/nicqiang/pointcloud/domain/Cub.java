package com.github.nicqiang.pointcloud.domain;

import lombok.Data;

import java.util.List;

/**
 * Created by
 *
 * @Author: nicqiang
 * @DATE: 2019/3/20
 */
@Data
public class Cub {

    /**
     * 点的索引
     */
    private List<Point> points;

    /**
     * 是否访问过
     */
    private boolean visit;
}
