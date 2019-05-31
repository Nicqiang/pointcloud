package com.github.nicqiang.pointcloud.domain;

import lombok.Data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by
 *
 * @Author: nicqiang
 * @DATE: 2019/3/26
 */
@Data
public class SimplifyKnStruct {

    private List<KNStruct> knStructs;

    public SimplifyKnStruct(List<KNStruct> knStructs) {
        this.knStructs = knStructs;
    }

    public void sort(){
        this.knStructs.sort((o1, o2) -> {
            return (o1.getKnNodes().get(0).getDistance() > o2.getKnNodes().get(0).getDistance()) ? -1 : 1;
        });
    }

    /**
     * 动态调整，保证优先队列
     */
    public void dynamicSort(){
        List<KNStruct>  structList = new LinkedList<>();
        knStructs.stream().forEach(knStruct -> {
            insert(structList,knStruct);
        });
        this.knStructs = structList;
    }

    private void insert(List<KNStruct> structList, KNStruct knStruct) {
        if(structList.size() == 0 || structList.get(structList.size()-1).getKnNodes().get(0).getDistance()
                < knStruct.getKnNodes().get(0).getDistance()){
            structList.add(knStruct);
            return;
        }

        for (int i = structList.size()-2; i >=0; i--) {
            if(structList.get(i).getKnNodes().get(0).getDistance() < knStruct.getKnNodes().get(0).getDistance()){
                structList.add(i,knStruct);
                return;
            }
        }

    }

    public void dynamicRemove(){
        Iterator<KNStruct> iterator = knStructs.iterator();
        while (iterator.hasNext()){
            KNStruct next = iterator.next();
            if(next.getPoint().isRemove()){
                iterator.remove();
                continue;
            }
            Iterator<KNNode> it = next.getKnNodes().iterator();
            while (it.hasNext()){
                if(it.next().getPoint().isRemove()){
                    it.remove();
                }
                break;
            }
        }
    }

}
