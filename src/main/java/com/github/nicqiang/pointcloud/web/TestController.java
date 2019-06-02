package com.github.nicqiang.pointcloud.web;

import com.github.nicqiang.pointcloud.algorithm.Denoising;
import com.github.nicqiang.pointcloud.algorithm.simplify.GridKdTreeSimplifyPointCloud;
import com.github.nicqiang.pointcloud.algorithm.simplify.MinDistSimplifyPointCloud;
import com.github.nicqiang.pointcloud.algorithm.simplify.PcaSimplifyPointCloud;
import com.github.nicqiang.pointcloud.algorithm.simplify.RandomSimplifyPointCloud;
import com.github.nicqiang.pointcloud.domain.Point;
import com.github.nicqiang.pointcloud.domain.PointCloud;
import com.github.nicqiang.pointcloud.points.DefaultCubPoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by
 *
 * @Author: nicqiang
 * @DATE: 2019/3/15
 */
@Slf4j
@RequestMapping("/test")
@RestController
public class TestController {

    /**
     * @return
     */
    @GetMapping("/points/cub")
    public PointCloud getCubPoint(@RequestParam(required = false) Boolean noise, @RequestParam(required = false) Boolean simplify){
        PointCloud pointCloud = null;
        if (noise == null || !noise){
            //pointCloud = DefaultCubPoint.getPointCloudWithUnBalance(10000);
            pointCloud = DefaultCubPoint.getPointCloud(1000);
            //pointCloud = DefaultCubPoint.getPointCloudWithNosiy(10000, 200);
            //pointCloud = Denoising.dbScanReoveNoise(pointCloud, 10, 0.2f);
        }else {
            pointCloud = DefaultCubPoint.getPointCloudWithNosiy(10000, 100);
        }

        if(simplify != null && simplify){
            //MinDistSimplifyPointCloud.simplify(pointCloud,20, 0.2f);
            PcaSimplifyPointCloud.simplify(pointCloud,15, 0.5f);
        }
        log.info("pointNum={}", pointCloud.getPointNum());
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
            RandomSimplifyPointCloud.simplify(pointCloud,factory);
            //GridKdTreeSimplifyPointCloud.simplify(pointCloud,factory);
        }
        return pointCloud;
    }

    /**
     * 读取龙的数据
     * @return
     * @throws IOException
     */
    @GetMapping("/points/dragon")
    public PointCloud getDragonPoint(@RequestParam(required = false) Float factory, @RequestParam(required = false) Boolean simplify) throws IOException {
        PointCloud pointCloud = new PointCloud();
        pointCloud = getCloudPointFromFile("classpath:points/dragon.txt");
        if(factory != null){
            RandomSimplifyPointCloud.simplify(pointCloud,factory);
        }
        if(simplify != null && simplify){
            MinDistSimplifyPointCloud.simplify(pointCloud, 20, 0.002f);
        }

        log.info("pointNum={}", pointCloud.getPointNum());
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

    @GetMapping("/points/cylinder")
    public PointCloud getCylinder(@RequestParam(required = false) Boolean simplify) throws IOException {
        PointCloud pointCloud = this.getCloudPointFromFile("classpath:points/cylinder_asc_point.txt");

        if(simplify != null && simplify){
            MinDistSimplifyPointCloud.simplify(pointCloud, 50, 0.2f);
        }
        log.info("pointNum={}", pointCloud.getPointNum());
        return  pointCloud;
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
        while (StringUtils.isNotEmpty(line)){
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
        removeRepeatPoint(pointCloud);
        return pointCloud;
    }

    /**
     * 点云去重
     * @param pointCloud
     */
    private void removeRepeatPoint(PointCloud pointCloud){
        List<String> points = Arrays.asList(pointCloud.getPoints().split(";"));
        points = new ArrayList<>(new HashSet<>(points));
        pointCloud.setPointNum((long)points.size());
        pointCloud.setPoints(String.join(";", points));
    }

}
