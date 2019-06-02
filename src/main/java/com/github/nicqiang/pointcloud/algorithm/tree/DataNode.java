package com.github.nicqiang.pointcloud.algorithm.tree;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by
 *
 * @Author: nicqiang
 * @DATE: 2019/3/28
 */
@Data
public class DataNode implements Serializable {
    private static final long serialVersionUID = 8810903836135881028L;
    /**
     * 多维度值
     */
    private float[] value;

    private boolean used;

    /**
     * 0未标记
     * 1核心节点
     * 2外点
     * 3噪点
     */
    private int scan = 0;

    private boolean delete;

    /**
     * 是否为特征点
     */
    private boolean feature;

    public DataNode(float[] value) {
        this.value = value;
    }


}
