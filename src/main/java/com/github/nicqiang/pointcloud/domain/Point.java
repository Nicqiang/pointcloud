package com.github.nicqiang.pointcloud.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by
 *
 * @Author: nicqiang
 * @DATE: 2019/3/20
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Point implements Serializable {
    private static final long serialVersionUID = -3793487211793392114L;

    private float x;

    private float y;

    private float z;

    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * 标记移除点
     */
    private boolean remove;

    public String transToString(){
        StringBuilder sb = new StringBuilder();
        sb.append(x);
        sb.append(",");
        sb.append(y);
        sb.append(",");
        sb.append(z);
        return sb.toString();
    }

}
