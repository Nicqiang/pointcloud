package com.github.nicqiang.pointcloud.domain;

import lombok.Data;

import java.util.List;

/**
 * Created by
 *
 * @Author: nicqiang
 * @DATE: 2019/3/25
 */
@Data
public class CubStruct {

    private List[][][] cubs;

    private int xNumber;

    private int yNumber;

    private int zNumber;



    /**
     * 边长
     */
    private float sideLength;
}
