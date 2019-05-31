package com.github.nicqiang.pointcloud.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by
 *
 * @Author: nicqiang
 * @DATE: 2019/3/20
 */
@Data
public class PointCloudInfo implements Serializable {
    private static final long serialVersionUID = 3798669715716287469L;

    private Integer total;

    private float xMax;

    private float xMin;

    private float yMax;

    private float yMin;

    private float zMax;

    private float zMin;


    public void init(){
        this.total = 0;
        this.xMax = 0;
        this.yMax = 0;
        this.zMax = 0;
        this.xMin = Integer.MAX_VALUE;
        this.yMin = Integer.MAX_VALUE;
        this.zMin = Integer.MAX_VALUE;
    }
}
