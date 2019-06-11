package com.github.nicqiang.pointcloud.service.impl;

import com.github.nicqiang.pointcloud.dao.PointCloudInfoDao;
import com.github.nicqiang.pointcloud.domain.PointCloud;
import com.github.nicqiang.pointcloud.repository.PointCloudInfo;
import com.github.nicqiang.pointcloud.service.PointCloudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-06-11
 */
@Service
@Slf4j
public class PointCloudServiceImpl implements PointCloudService {

    @Autowired
    private PointCloudInfoDao dao;

    @Override
    public void save(PointCloudInfo pointCloudInfo) {
        dao.save(pointCloudInfo);
    }

    @Override
    public void edit(PointCloudInfo pointCloudInfo) {
        dao.save(pointCloudInfo);
    }

    @Override
    public void delete(Long id) {
        dao.deleteById(id);

    }

    @Override
    public PointCloudInfo getById(Long id) {
        return dao.findById(id).get();
    }

    @Override
    public List<PointCloudInfo> getAll() {
        return dao.findAll();
    }

    @Override
    public Page<PointCloudInfo> getAll(Pageable pageable) {
        return dao.findAll(pageable);
    }
}
