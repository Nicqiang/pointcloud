package com.github.nicqiang.pointcloud.algorithm.simplify;

import com.github.nicqiang.pointcloud.domain.PointCloud;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by
 *
 * @Author: nicqiang
 * @DATE: 2019/3/17
 */
@Slf4j
public class RandomSimplifyPointCloud {

    private static final Random random = new Random(System.currentTimeMillis());

    public static void simplify(PointCloud pointCloud,final Float factory){
        long startTime = System.currentTimeMillis();

        try {

            String str = Arrays.stream(pointCloud.getPoints().split(";")).filter(x-> random.nextDouble()< factory).collect(Collectors.joining(";"));
            pointCloud.setPoints(str);
        }finally {
            log.info("RandomSimplifyPointCloud||cost={}",System.currentTimeMillis()-startTime);
        }
    }


}
