package com.github.nicqiang.pointcloud.domain;

import lombok.Data;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by
 *
 * @Author: nicqiang
 * @DATE: 2019/3/25
 */
@Data
public class KNStruct {

    private Point point;

    /**
     * 从小到大排列
     */
    private List<KNNode> knNodes = new LinkedList<>();



    /**
     * 插入点
     * @param knNode
     */
    public void insertKNPoint(KNNode knNode,int kNumber){
        boolean insertFlag = false;
        for (int i = 0; i < kNumber && i < this.knNodes.size(); i++) {
            if(knNodes.get(i).getDistance()>knNode.getDistance()){
                knNodes.add(i,knNode);
                insertFlag = true;
                break;
            }
        }
        if(!insertFlag){
            if(this.knNodes.size() < kNumber){
                this.knNodes.add(knNode);
            }
        }else {
            if(this.knNodes.size() > kNumber){
                this.knNodes.remove(kNumber);
            }
        }

    }

}
