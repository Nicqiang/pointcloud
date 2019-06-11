package com.github.nicqiang.pointcloud.web;

import com.github.nicqiang.pointcloud.common.BaseResponse;
import com.github.nicqiang.pointcloud.common.BaseResponseBuilder;
import com.github.nicqiang.pointcloud.service.impl.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-06-11
 */
@Slf4j
@RequestMapping("/file")
@Controller
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    //upload
    @PostMapping("/upload")
    @ResponseBody
    public BaseResponse handleFileUpload(@RequestParam("file") MultipartFile file){
        String filePath = null;
        try {
            filePath = fileStorageService.store(file);
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("store failed", e);
            return BaseResponseBuilder.exception(500, e.getMessage());
        }
        return BaseResponseBuilder.success(filePath);
    }

    /**
     * 下载文件
     * @param filePath file path
     * @return
     */
    @GetMapping("download/{filePath}")
    public ResponseEntity<Resource> getFile(@PathVariable String filePath){
        Resource file = fileStorageService.loadAsResource(filePath);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);

    }
}
