package com.github.nicqiang.pointcloud.algorithm.simplify;

import com.github.nicqiang.pointcloud.domain.*;
import com.github.nicqiang.pointcloud.utils.PointsUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by
 *
 * @Author: nicqiang
 * @DATE: 2019/3/20
 */
@Slf4j
public class GridKdTreeSimplifyPointCloud {


    public static void simplify(PointCloud pointCloud, final Float factory){
        //构建
        List<Point> points = PointsUtil.toPoints(pointCloud.getPoints());

        PointCloudInfo pointInfo = PointsUtil.getPointInofFormPointCloud(points);
        PointsUtil.scaleUppBoxSize(pointInfo);
        long cubStartTime = System.currentTimeMillis();
        //划分空间块
        CubStruct cubStruct = cubIndexs(points,pointInfo);
        log.info("cubTime={}",System.currentTimeMillis() - cubStartTime);

        long searchKnTime = System.currentTimeMillis();
        //寻找k近邻
        List<KNStruct> knStructs = buildKNStruct(points,cubStruct,pointInfo);
        log.info("searchKnTime={}",System.currentTimeMillis() - searchKnTime);



        long simplifyTime = System.currentTimeMillis();
        //精简
        simplifyPoints(knStructs,points,factory);
        log.info("simplifyTime={}",System.currentTimeMillis() - simplifyTime);

        //输出点
        printPoint(pointCloud,points);


    }

    /**
     * 简化点
     * @param knStructs
     * @param points
     */
    private static void simplifyPoints(List<KNStruct> knStructs, List<Point> points,Float factor) {
        int pointNumbers = (int)Math.ceil(points.size() * factor);
        double minDist = Double.MAX_VALUE;
        //排序
        int exisitPointsNum = points.size();
        SimplifyKnStruct simplifyKnStruct = new SimplifyKnStruct(knStructs);
        simplifyKnStruct.sort();
        while (pointNumbers < exisitPointsNum){
            simplifyKnStruct.getKnStructs().get(0).getPoint().setRemove(true);
            exisitPointsNum --;
            simplifyKnStruct.dynamicRemove();
            simplifyKnStruct.dynamicSort();
        }

    }

    private static long getExsitPointsNum(List<Point> points) {
        return points.stream().filter(point -> !point.isRemove()).count();
    }

    /**
     * 输出
     * @param pointCloud
     * @param points
     */
    private static void printPoint(PointCloud pointCloud, List<Point> points) {
        StringBuilder sb = new StringBuilder();
        long count = points.stream().filter(point -> {
            if (!point.isRemove()) {
                sb.append(point.transToString());
                sb.append(";");
                return true;
            }
            return false;
        }).count();
        sb.deleteCharAt(sb.length()-1);
        pointCloud.setPointNum(count);
        pointCloud.setPoints(sb.toString());
    }

    /**
     * 构建k近邻结构
     * @param cubStruct
     * @return
     */
    private static List<KNStruct> buildKNStruct(List<Point> points,CubStruct cubStruct,PointCloudInfo pointInfo) {

        //k-length
        int kNumber =15;

        List<KNStruct> knStructs = initKnStructs(points);

        knStructs.stream().parallel().forEach(knStruct -> {
            int i = (int)Math.floor((knStruct.getPoint().getX() - pointInfo.getXMin())/cubStruct.getSideLength());
            int j = (int)Math.floor((knStruct.getPoint().getY() - pointInfo.getYMin())/cubStruct.getSideLength());
            int k = (int)Math.floor((knStruct.getPoint().getZ() - pointInfo.getZMin())/cubStruct.getSideLength());

            float xLen = (knStruct.getPoint().getX() - pointInfo.getXMin()) - cubStruct.getSideLength() * i;
            float yLen = (knStruct.getPoint().getY() - pointInfo.getYMin()) - cubStruct.getSideLength() * j;
            float zLen = (knStruct.getPoint().getZ() - pointInfo.getZMin()) - cubStruct.getSideLength() * k;

            List<Point> nearPoint = new ArrayList<>();
            nearPoint.addAll(cubStruct.getCubs()[i][j][k]);


            final float sideHalfLength = cubStruct.getSideLength()/2;


            if(xLen == sideHalfLength || yLen == sideHalfLength || zLen == sideHalfLength){
                if(xLen == sideHalfLength && yLen == sideHalfLength && zLen == sideHalfLength){
                    //nothing to do
                }else if(xLen == sideHalfLength && yLen == sideHalfLength ){
                    if(zLen > sideHalfLength){
                        nearPoint.addAll(getNearPoints(cubStruct,i,j,k+1));
                    }else {
                        nearPoint.addAll(getNearPoints(cubStruct,i,j,k-1));
                    }

                }else if(xLen == sideHalfLength && zLen == sideHalfLength){
                    if(yLen > sideHalfLength){
                        nearPoint.addAll(getNearPoints(cubStruct,i,j+1,k));
                    }else {
                        nearPoint.addAll(getNearPoints(cubStruct,i,j-1,k));
                    }
                }else if(yLen == sideHalfLength && zLen == sideHalfLength){
                    if(xLen>sideHalfLength){
                        nearPoint.addAll(getNearPoints(cubStruct,i+1,j,k));
                    }else {
                        nearPoint.addAll(getNearPoints(cubStruct,i-1,j,k));
                    }
                }else if(xLen == sideHalfLength){
                    if(yLen > sideHalfLength){
                        nearPoint.addAll(getNearPoints(cubStruct,i,j+1,k));
                        if(zLen > sideHalfLength){
                            nearPoint.addAll(getNearPoints(cubStruct,i,j,k+1));
                            nearPoint.addAll(getNearPoints(cubStruct,i,j+1,k+1));
                        }else {
                            nearPoint.addAll(getNearPoints(cubStruct,i,j,k-1));
                            nearPoint.addAll(getNearPoints(cubStruct,i,j+1,k-1));
                        }
                    }else {
                        nearPoint.addAll(getNearPoints(cubStruct,i,j-1,k));
                        if(zLen > sideHalfLength){
                            nearPoint.addAll(getNearPoints(cubStruct,i,j-1,k+1));
                            nearPoint.addAll(getNearPoints(cubStruct,i,j,k+1));
                        }else {
                            nearPoint.addAll(getNearPoints(cubStruct,i,j-1,k-1));
                            nearPoint.addAll(getNearPoints(cubStruct,i,j,k-1));
                        }
                    }
                }else if(yLen == sideHalfLength){
                    if(xLen > sideHalfLength){
                        nearPoint.addAll(getNearPoints(cubStruct,i+1,j,k));
                        if(zLen > sideHalfLength){
                            nearPoint.addAll(getNearPoints(cubStruct,i,j,k+1));
                            nearPoint.addAll(getNearPoints(cubStruct,i+1,j,k+1));
                        }else {
                            nearPoint.addAll(getNearPoints(cubStruct,i,j,k-1));
                            nearPoint.addAll(getNearPoints(cubStruct,i+1,j,k-1));
                        }
                    }else {
                        nearPoint.addAll(getNearPoints(cubStruct,i-1,j,k));
                        if(zLen > sideHalfLength){
                            nearPoint.addAll(getNearPoints(cubStruct,i,j,k+1));
                            nearPoint.addAll(getNearPoints(cubStruct,i-1,j,k+1));
                        }else {
                            nearPoint.addAll(getNearPoints(cubStruct,i,j,k-1));
                            nearPoint.addAll(getNearPoints(cubStruct,i-1,j,k-1));
                        }
                    }

                }else if(zLen == sideHalfLength){
                    if(xLen > sideHalfLength){
                        nearPoint.addAll(getNearPoints(cubStruct,i+1,j,k));
                        if(yLen > sideHalfLength){
                            nearPoint.addAll(getNearPoints(cubStruct,i,j+1,k));
                            nearPoint.addAll(getNearPoints(cubStruct,i+1,j+1,k));
                        }else {
                            nearPoint.addAll(getNearPoints(cubStruct,i,j-1,k));
                            nearPoint.addAll(getNearPoints(cubStruct,i+1,j-1,k));
                        }
                    }else {
                        nearPoint.addAll(getNearPoints(cubStruct,i-1,j,k));
                        if(yLen > sideHalfLength){
                            nearPoint.addAll(getNearPoints(cubStruct,i-1,j+1,k));
                            nearPoint.addAll(getNearPoints(cubStruct,i,j+1,k));
                        }else {
                            nearPoint.addAll(getNearPoints(cubStruct,i-1,j-1,k));
                            nearPoint.addAll(getNearPoints(cubStruct,i,j-1,k));
                        }

                    }

                }

            }else if(xLen > sideHalfLength && yLen > sideHalfLength && zLen > sideHalfLength){
                nearPoint.addAll(getNearPoints(cubStruct,i,j,k+1));
                nearPoint.addAll(getNearPoints(cubStruct,i,j+1,k));
                nearPoint.addAll(getNearPoints(cubStruct,i,j+1,k+1));
                nearPoint.addAll(getNearPoints(cubStruct,i+1,j,k));
                nearPoint.addAll(getNearPoints(cubStruct,i+1,j,k+1));
                nearPoint.addAll(getNearPoints(cubStruct,i+1,j+1,k));
                nearPoint.addAll(getNearPoints(cubStruct,i+1,j+1,k+1));
            }else if(xLen > sideHalfLength && yLen > sideHalfLength && zLen < sideHalfLength){
                nearPoint.addAll(getNearPoints(cubStruct,i,j,k-1));
                nearPoint.addAll(getNearPoints(cubStruct,i,j+1,k-1));
                nearPoint.addAll(getNearPoints(cubStruct,i,j+1,k));
                nearPoint.addAll(getNearPoints(cubStruct,i+1,j,k-1));
                nearPoint.addAll(getNearPoints(cubStruct,i+1,j,k));
                nearPoint.addAll(getNearPoints(cubStruct,i+1,j+1,k-1));
                nearPoint.addAll(getNearPoints(cubStruct,i+1,j+1,k));
            }else if(xLen >sideHalfLength && yLen < sideHalfLength && zLen > sideHalfLength){
                nearPoint.addAll(getNearPoints(cubStruct,i,j-1,k));
                nearPoint.addAll(getNearPoints(cubStruct,i,j-1,k+1));
                nearPoint.addAll(getNearPoints(cubStruct,i,j,k+1));
                nearPoint.addAll(getNearPoints(cubStruct,i+1,j-1,k));
                nearPoint.addAll(getNearPoints(cubStruct,i+1,j-1,k+1));
                nearPoint.addAll(getNearPoints(cubStruct,i+1,j,k));
                nearPoint.addAll(getNearPoints(cubStruct,i+1,j,k+1));
            }else if(xLen < sideHalfLength && yLen > sideHalfLength && zLen > sideHalfLength){
                nearPoint.addAll(getNearPoints(cubStruct,i-1,j,k));
                nearPoint.addAll(getNearPoints(cubStruct,i-1,j,k+1));
                nearPoint.addAll(getNearPoints(cubStruct,i-1,j+1,k));
                nearPoint.addAll(getNearPoints(cubStruct,i-1,j+1,k+1));
                nearPoint.addAll(getNearPoints(cubStruct,i,j,k+1));
                nearPoint.addAll(getNearPoints(cubStruct,i,j+1,k));
                nearPoint.addAll(getNearPoints(cubStruct,i,j+1,k+1));
            }else if(xLen > sideHalfLength && yLen < sideHalfLength && zLen < sideHalfLength){
                nearPoint.addAll(getNearPoints(cubStruct,i,j-1,k-1));
                nearPoint.addAll(getNearPoints(cubStruct,i,j-1,k));
                nearPoint.addAll(getNearPoints(cubStruct,i,j,k-1));
                nearPoint.addAll(getNearPoints(cubStruct,i+1,j-1,k-1));
                nearPoint.addAll(getNearPoints(cubStruct,i+1,j-1,k));
                nearPoint.addAll(getNearPoints(cubStruct,i+1,j,k-1));
                nearPoint.addAll(getNearPoints(cubStruct,i+1,j,k));
            }else if(xLen < sideHalfLength && yLen > sideHalfLength && zLen < sideHalfLength){
                nearPoint.addAll(getNearPoints(cubStruct,i-1,j,k-1));
                nearPoint.addAll(getNearPoints(cubStruct,i-1,j,k));
                nearPoint.addAll(getNearPoints(cubStruct,i-1,j+1,k-1));
                nearPoint.addAll(getNearPoints(cubStruct,i-1,j+1,k));
                nearPoint.addAll(getNearPoints(cubStruct,i,j,k-1));
                nearPoint.addAll(getNearPoints(cubStruct,i,j+1,k-1));
                nearPoint.addAll(getNearPoints(cubStruct,i,j+1,k));
            }else if(xLen < sideHalfLength && yLen < sideHalfLength && zLen > sideHalfLength){
                nearPoint.addAll(getNearPoints(cubStruct,i-1,j-1,k));
                nearPoint.addAll(getNearPoints(cubStruct,i-1,j-1,k+1));
                nearPoint.addAll(getNearPoints(cubStruct,i-1,j,k));
                nearPoint.addAll(getNearPoints(cubStruct,i-1,j,k+1));
                nearPoint.addAll(getNearPoints(cubStruct,i,j-1,k));
                nearPoint.addAll(getNearPoints(cubStruct,i,j-1,k+1));
                nearPoint.addAll(getNearPoints(cubStruct,i,j,k+1));
            }else {
                nearPoint.addAll(getNearPoints(cubStruct,i-1,j-1,k-1));
                nearPoint.addAll(getNearPoints(cubStruct,i-1,j-1,k));
                nearPoint.addAll(getNearPoints(cubStruct,i-1,j,k-1));
                nearPoint.addAll(getNearPoints(cubStruct,i-1,j,k));
                nearPoint.addAll(getNearPoints(cubStruct,i,j-1,k-1));
                nearPoint.addAll(getNearPoints(cubStruct,i,j-1,k));
                nearPoint.addAll(getNearPoints(cubStruct,i,j,k-1));
            }

            buildKNNodes(nearPoint,knStruct,kNumber);
        });

        return knStructs;


    }

    private static void buildKNNodes(List<Point> nearPoint, KNStruct knStruct,int kNumber) {
        Point centerPoint = knStruct.getPoint();
        nearPoint.forEach(point -> {
            double distance = Math.sqrt(Math.pow(centerPoint.getX()-point.getX(),2)
                    + Math.pow(centerPoint.getY()-point.getY(),2) + Math.pow(centerPoint.getZ()-point.getZ(),2));
            //排除自身和重复的点
            if(distance != 0){
                knStruct.insertKNPoint(new KNNode(distance, point),kNumber);
            }
        });
    }

    private static List<? extends Point> getNearPoints(CubStruct cubStruct, int i, int j, int k) {
        if(i>=0 && i<cubStruct.getXNumber() && j>=0 && j<cubStruct.getYNumber() && k>=0 && k<cubStruct.getZNumber()){
            if(cubStruct.getCubs()[i][j][k] != null){
                return cubStruct.getCubs()[i][j][k];
            }
        }
        return new LinkedList<>();
    }

    /**
     * 初始化
     * @param points
     * @return
     */
    private static List<KNStruct> initKnStructs(List<Point> points) {
        List<KNStruct> knStructs = new LinkedList<>();
        points.stream().forEach(point -> {
            KNStruct knStruct = new KNStruct();
            knStruct.setPoint(point);
            knStructs.add(knStruct);
        });
        return knStructs;
    }

    /**
     * 构建，局部排序
     * @param points
     * @param info
     * @return
     */
    private static CubStruct cubIndexs(List<Point> points,PointCloudInfo info){

        float f = (info.getXMax() - info.getXMin())/50;
        int x = (int)Math.ceil((info.getXMax() - info.getXMin())/f);
        int y = (int)Math.ceil((info.getYMax() - info.getYMin())/f);
        int z = (int)Math.ceil((info.getZMax() - info.getZMin())/f);
        List[][][] cubIndex = new ArrayList<?>[x][y][z];

        points.stream().forEach(point -> {
            int cellX = (int)Math.floor((point.getX() - info.getXMin())/f);
            int cellY = (int)Math.floor((point.getY() - info.getYMin())/f);
            int cellZ = (int)Math.floor((point.getZ() - info.getZMin())/f);
            if(cubIndex[cellX][cellY][cellZ] == null){
                cubIndex[cellX][cellY][cellZ] = new ArrayList();
            }
            cubIndex[cellX][cellY][cellZ].add(point);
        });

        CubStruct cubStruct = new CubStruct();
        cubStruct.setCubs(cubIndex);
        cubStruct.setSideLength(f);
        cubStruct.setXNumber(x);
        cubStruct.setYNumber(y);
        cubStruct.setZNumber(z);
        return cubStruct;


    }

}
