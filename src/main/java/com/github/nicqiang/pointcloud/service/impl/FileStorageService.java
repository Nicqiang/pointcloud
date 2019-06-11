package com.github.nicqiang.pointcloud.service.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <h></h>
 * 文件存储服务
 * @author nicqiang
 * @since 2019-06-11
 */
public interface FileStorageService  extends ApplicationContextAware {


    /**
     * load resource
     * @param filePath
     * @return
     */
    Resource loadAsResource(String filePath);

    /**
     * store file
     * @param file file
     * @return file store path
     */
    String store(MultipartFile file) throws IOException;
}
