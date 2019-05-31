package com.github.nicqiang.pointcloud.algorithm.tree;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by
 *
 * @Author: nicqiang
 * @DATE: 2019/3/28
 */
@Slf4j
public class KdTreeTest {


    @Test
    public void testCreateTree(){
        List<DataNode> data = getData();
        KdTree kdTree = new KdTree(data);
        log.info("finished");
    }

    private List<DataNode> getData() {
        List<DataNode> list = new ArrayList<>();
        float[] d1 = {7,2};
        float[] d2 = {5,4};
        float[] d3 = {2,3};
        float[] d4 = {8,1};
        float[] d5 = {9,6};
        float[] d6 = {4,7};
        list.add(new DataNode(d1));
        list.add(new DataNode(d2));
        list.add(new DataNode(d3));
        list.add(new DataNode(d4));
        list.add(new DataNode(d5));
        list.add(new DataNode(d6));
        return list;
    }

    @Test
    public void testGetKNearestDataNode(){
        List<DataNode> data = getData();
        KdTree kdTree = new KdTree(data);
        float[] d1 = {2f,4.5f};
        DataNode dataNode = new DataNode(d1);
        List<DataNode> kNearestDataNode = kdTree.getKNearestDataNode(dataNode, 3);
        System.out.println(JSONObject.toJSON(kNearestDataNode));
    }

}