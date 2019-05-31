package com.github.nicqiang.pointcloud.utils;

import com.github.nicqiang.pointcloud.domain.Point;
import com.github.nicqiang.pointcloud.domain.PointCloudInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by
 *
 * @Author: nicqiang
 * @DATE: 2019/3/20
 */
public class PointsUtil {


    /**
     * string to points
     * @param string
     * @return
     */
    public static List<Point> toPoints(String string){
        List<Point> points = new ArrayList<>();
        Arrays.stream(string.split(";")).forEach(s -> {
            String[] split = s.split(",");
            Point point = new Point(Float.valueOf(split[0]), Float.valueOf(split[1]), Float.valueOf(split[2]));
            points.add(point);
        });
        return points;

    }


    public static PointCloudInfo getPointInofFormPointCloud(List<Point> points){
        PointCloudInfo pointCloudInfo = new PointCloudInfo();
        pointCloudInfo.init();
        pointCloudInfo.setTotal(points.size());
        points.stream().forEach(point -> {
            if(point.getX() < pointCloudInfo.getXMin()){
                pointCloudInfo.setXMin(point.getX());
            }else if(point.getX() > pointCloudInfo.getXMax()){
                pointCloudInfo.setXMax(point.getX());
            }

            if(point.getY() < pointCloudInfo.getYMin()){
                pointCloudInfo.setYMin(point.getY());
            }else if(point.getY() > pointCloudInfo.getYMax()){
                pointCloudInfo.setYMax(point.getY());
            }

            if(point.getZ() < pointCloudInfo.getZMin()){
                pointCloudInfo.setZMin(point.getZ());
            }else if(point.getZ() > pointCloudInfo.getZMax()){
                pointCloudInfo.setZMax(point.getZ());
            }
        });

        return pointCloudInfo;

    }


    /**
     * 扩大边界
     * @param pointInfo {@link PointCloudInfo}
     */
    public static void scaleUppBoxSize(PointCloudInfo pointInfo) {
        float x = (pointInfo.getXMax() - pointInfo.getXMin())/500;
        float y = (pointInfo.getYMax() - pointInfo.getYMin())/500;
        float z = (pointInfo.getZMax() - pointInfo.getZMin())/500;

        pointInfo.setXMax(pointInfo.getXMax() + x);
        pointInfo.setXMin(pointInfo.getXMin() - x);
        pointInfo.setYMax(pointInfo.getYMax() + y);
        pointInfo.setYMin(pointInfo.getYMin() - y);
        pointInfo.setZMax(pointInfo.getZMax() + z);
        pointInfo.setZMin(pointInfo.getZMin() - z);
    }
}
