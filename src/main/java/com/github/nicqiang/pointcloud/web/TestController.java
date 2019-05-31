package com.github.nicqiang.pointcloud.web;

import com.github.nicqiang.pointcloud.algorithm.Denoising;
import com.github.nicqiang.pointcloud.algorithm.simplify.GridKdTreeSimplifyPointCloud;
import com.github.nicqiang.pointcloud.algorithm.simplify.RandomSimplifyPointCloud;
import com.github.nicqiang.pointcloud.domain.Point;
import com.github.nicqiang.pointcloud.domain.PointCloud;
import com.github.nicqiang.pointcloud.points.DefaultCubPoint;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by
 *
 * @Author: nicqiang
 * @DATE: 2019/3/15
 */
@RequestMapping("/test")
@RestController
public class TestController {

    /**
     * @return
     */
    @GetMapping("/points/cub")
    public PointCloud getCubPoint(@RequestParam(required = false) Boolean noise){
        PointCloud pointCloud = null;
        if (noise == null || !noise){
            //pointCloud = DefaultCubPoint.getPointCloud(10000);
            pointCloud = DefaultCubPoint.getPointCloudWithNosiy(10000, 100);
            pointCloud = Denoising.dbScanReoveNoise(pointCloud);
        }else {
            pointCloud = DefaultCubPoint.getPointCloudWithNosiy(10000, 100);
        }
        return pointCloud;
    }

    /**
     *
     * 读取兔子
     * @return
     * @throws IOException
     */
    @GetMapping("/points/bunny")
    public PointCloud getBunnyPoint(@RequestParam(required = false) Float factory) throws IOException {
        PointCloud pointCloud = new PointCloud();
        pointCloud = getCloudPointFromFile("classpath:points/bunny.txt");
        if(factory != null){
            //RandomSimplifyPointCloud.simplify(pointCloud,factory);
            GridKdTreeSimplifyPointCloud.simplify(pointCloud,factory);
        }
        return pointCloud;
    }

    /**
     * 读取龙的数据
     * @return
     * @throws IOException
     */
    @GetMapping("/points/dragon")
    public PointCloud getDragonPoint(@RequestParam(required = false) Float factory) throws IOException {
        PointCloud pointCloud = new PointCloud();
        pointCloud = getCloudPointFromFile("classpath:points/dragon.txt");
        if(factory != null){
            RandomSimplifyPointCloud.simplify(pointCloud,factory);
        }
        return pointCloud;
    }


    /**
     * 读取龙的数据
     * @return
     * @throws IOException
     */
    @GetMapping("/points/hand")
    public PointCloud getHandPoint(@RequestParam(required = false) Float factory) throws IOException {
        PointCloud pointCloud = new PointCloud();
        pointCloud = getCloudPointFromFile("classpath:points/hand.txt");
        if(factory != null){
            RandomSimplifyPointCloud.simplify(pointCloud,factory);
        }
        return pointCloud;
    }

    /**
     * get point from file
     * @param filePath
     * @return
     * @throws IOException
     */
    private PointCloud getCloudPointFromFile(String filePath) throws IOException {
        File file = ResourceUtils.getFile(filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
        BufferedReader bf = new BufferedReader(inputStreamReader);
        String line = bf.readLine();
        StringBuilder sb = new StringBuilder();
        long count = 0;
        while (line != null){
            count++;
            String[] s = line.split(" ");
            for (int i = 0; i < 3; i++) {
                sb.append(s[i]);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append(";");
            line = bf.readLine();
        }
        sb.deleteCharAt(sb.length()-1);
        PointCloud pointCloud = new PointCloud();
        pointCloud.setPointNum(count);
        pointCloud.setPoints(sb.toString());
        return pointCloud;
    }

}
