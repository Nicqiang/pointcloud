package com.github.nicqiang.pointcloud.common;

import lombok.Data;

import java.io.Serializable;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-06-11
 */
@Data
public class PageInfo implements Serializable {
    private static final long serialVersionUID = 8302543118929286113L;

    Integer page;

    Integer size;

}
