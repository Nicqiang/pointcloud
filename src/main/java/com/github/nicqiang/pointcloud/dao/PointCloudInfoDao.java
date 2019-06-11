package com.github.nicqiang.pointcloud.dao;

import com.github.nicqiang.pointcloud.repository.PointCloudInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-06-11
 */
@Component
public interface PointCloudInfoDao extends JpaRepository<PointCloudInfo, Long> {

}
