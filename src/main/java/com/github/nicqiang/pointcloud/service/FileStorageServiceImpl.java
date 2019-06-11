package com.github.nicqiang.pointcloud.service;

import com.github.nicqiang.pointcloud.service.impl.FileStorageService;
import com.github.nicqiang.pointcloud.utils.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-06-11
 */
@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService{

    @Value("/Users/nicqiang/tmp/fileStore/")
    private String desPath;

    private ApplicationContext context;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }


    @Override
    public Resource loadAsResource(String filePath) {
        String detailFilePath = this.desPath + filePath;
        Resource resource = context.getResource("file:" + detailFilePath);
        return resource;
    }

    @Override
    public String store(MultipartFile file) throws IOException {
        String fileName = UuidUtil.gen() + "-" + file.getOriginalFilename();
        String desFilePath = this.desPath + fileName;
        File desFile = new File(desFilePath);
        try {
            file.transferTo(desFile);
        } catch (IOException e) {
            log.error("store file failed, file=" + file.getName());
            throw e;
        }
        return fileName;
    }
}
