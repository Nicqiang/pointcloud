package com.github.nicqiang.pointcloud.service;

import com.github.nicqiang.pointcloud.domain.PointCloud;
import com.github.nicqiang.pointcloud.repository.PointCloudInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-06-11
 */
public interface PointCloudService {

    void save(PointCloudInfo pointCloudInfo);

    void edit(PointCloudInfo pointCloudInfo);

    void delete(Long id);

    PointCloudInfo getById(Long id);

    List<PointCloudInfo> getAll();

    Page<PointCloudInfo> getAll(Pageable pageable);
}
