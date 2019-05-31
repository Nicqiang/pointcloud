package com.github.nicqiang.pointcloud;

import org.junit.Test;

import java.util.Random;

/**
 * Created by
 *
 * @Author: nicqiang
 * @DATE: 2019/3/15
 */
public class RamdomTest {

    @Test
    public void test1(){
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 10; i++) {
            System.out.println(random.nextDouble());
        }
    }
}
