package com.github.nicqiang.pointcloud.utils;

import java.util.UUID;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-06-11
 */
public class UuidUtil {

    /**
     * 生成的uuid
     * @return
     */
    public static String gen(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
