package com.github.nicqiang.pointcloud.web;

import com.alibaba.fastjson.JSONObject;
import com.github.nicqiang.pointcloud.common.BaseResponse;
import com.github.nicqiang.pointcloud.common.BaseResponseBuilder;
import com.github.nicqiang.pointcloud.common.PageInfo;
import com.github.nicqiang.pointcloud.repository.PointCloudInfo;
import com.github.nicqiang.pointcloud.service.PointCloudService;
import com.github.nicqiang.pointcloud.service.impl.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-06-11
 */
@Slf4j
@RequestMapping("/api")
@Controller
public class ApiController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PointCloudService pointCloudService;

    @PostMapping("/points")
    @ResponseBody
    public BaseResponse savePointInfo(@RequestBody PointCloudInfo pointCloudInfo){
        pointCloudService.save(pointCloudInfo);
        log.info("inset point cloud info,{}", JSONObject.toJSONString(pointCloudInfo));
        return BaseResponseBuilder.success();
    }

    @PutMapping("/points")
    @ResponseBody
    public BaseResponse editPointInfo(@RequestBody PointCloudInfo pointCloudInfo){
        pointCloudService.save(pointCloudInfo);
        return BaseResponseBuilder.success();
    }


    @DeleteMapping("/points/{id}")
    @ResponseBody
    public BaseResponse deletePointInfo(@PathVariable("id") Long id){
        pointCloudService.delete(id);
        return BaseResponseBuilder.success();
    }


    @GetMapping("/points")
    @ResponseBody
    public BaseResponse getAll(){
        List<PointCloudInfo> all = pointCloudService.getAll();
        return BaseResponseBuilder.success(all);
    }

    @GetMapping("/pointpage")
    @ResponseBody
    public BaseResponse getAll(PageInfo pageInfo){
        if(pageInfo.getPage() == null){
            pageInfo.setPage(0);
        }
        if(pageInfo.getSize() == null){
            pageInfo.setSize(10);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize(), sort);
        Page<PointCloudInfo> pointCloudInfos = pointCloudService.getAll(pageable);
        return BaseResponseBuilder.success(pointCloudInfos);


    }

    @GetMapping("/points/{id}")
    @ResponseBody
    public BaseResponse getById(@PathVariable("id") Long id){
        PointCloudInfo pointCloudInfo = pointCloudService.getById(id);
        return BaseResponseBuilder.success(pointCloudInfo);
    }




    //upload
    @PostMapping("/file")
    public BaseResponse handleFileUpload(@RequestParam("file")MultipartFile file){
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
    @GetMapping("file/{filePath}")
    public ResponseEntity<Resource> getFile(@PathVariable String filePath){
        Resource file = fileStorageService.loadAsResource(filePath);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);

    }
}
