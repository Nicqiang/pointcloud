package com.github.nicqiang.pointcloud.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by
 *
 * @Author: nicqiang
 * @DATE: 2019/3/15
 */
@Data
public class PointCloud implements Serializable {
    private static final long serialVersionUID = 1244732802694663053L;

    /**
     * 点数
     */
    private Long pointNum;

    /**
     * 点,存储格式如下
     * x,y,z;x,y,z
     */
    private String points;
}
