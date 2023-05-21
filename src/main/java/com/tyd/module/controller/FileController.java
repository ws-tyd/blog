package com.tyd.module.controller;


import com.tyd.common.config.MinioConfig;
import com.tyd.common.exception.CustomaizeExpetion;
import com.tyd.common.exception.ExceptionEnum;
import com.tyd.common.util.BaseUtils;
import com.tyd.common.util.MinioUtil;
import com.tyd.common.util.Result;
import com.tyd.module.pojo.Image;
import com.tyd.module.pojo.Vo.FileVo;
import com.tyd.module.service.ImageService;
import io.minio.messages.Bucket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "文件相关接口")
@Slf4j
@RestController
@RequestMapping(value = "product/file")
public class FileController {


    private final MinioUtil minioUtil;
    private final MinioConfig prop;
    private final ImageService imageService;

    public FileController(MinioUtil minioUtil, MinioConfig prop, ImageService imageService) {
        this.minioUtil = minioUtil;
        this.prop = prop;
        this.imageService = imageService;
    }

    @ApiOperation(value = "查看存储bucket是否存在")
    @GetMapping("/bucketExists")
    public Result bucketExists(@RequestParam("bucketName") String bucketName) {
        HashMap<String, Object> ma = new HashMap<>();
        Thread thread = new Thread();
        ma.put("bucketName", minioUtil.bucketExists(bucketName));
        return Result.ok();
    }

    @ApiOperation(value = "创建存储bucket")
    @GetMapping("/makeBucket")
    public Result makeBucket(String bucketName) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("bucketName", minioUtil.makeBucket(bucketName));
        return Result.ok(map);
    }

    @ApiOperation(value = "删除存储bucket")
    @GetMapping("/removeBucket")
    public Result removeBucket(String bucketName) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("bucketName", minioUtil.removeBucket(bucketName));
        return Result.ok(map);
    }

    @ApiOperation(value = "获取全部bucket")
    @GetMapping("/getAllBuckets")
    public Result getAllBuckets() {
        List<Bucket> allBuckets = minioUtil.getAllBuckets();
        List<String> names = new ArrayList<>();
        allBuckets.forEach(item ->{
            names.add(item.name());
        });
        return Result.ok(names);
    }

    @ApiOperation(value = "文件上传返回url,选择桶")
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file,@RequestParam("bucketName")String bucketName) {
        bucketName = BaseUtils.isBlank(bucketName)? prop.getBucketName():bucketName;
        String objectName = minioUtil.upload(file,bucketName);
        if (null != objectName) {
            Map<String, Object> map = new HashMap<>();
            imageService.saveImage(prop.getEndpoint(),bucketName,objectName);
            map.put("url", (prop.getEndpoint() + "/" + bucketName + "/" + objectName));
            log.info("文件上传成功保存位置为 {}", (prop.getEndpoint() + "/" + bucketName + "/" + objectName));
            return Result.ok(map);
        }
        return Result.fail();
    }

    @ApiOperation(value = "图片/视频预览")
    @GetMapping("/preview")
    public Result preview(@RequestParam("fileName") String fileName,String bucketName) {
        HashMap<String, Object> map = new HashMap<>();
        if (BaseUtils.isBlank(bucketName)){
            bucketName = prop.getBucketName();
        }
        if (!minioUtil.bucketExists(bucketName)){
            throw new CustomaizeExpetion(ExceptionEnum.NOT_FOUND);
        }
        return Result.ok(minioUtil.preview(fileName,bucketName));
    }

    @ApiOperation(value = "文件下载")
    @GetMapping("/download")
    public Result download(@RequestParam("fileName") String fileName,String bucketName,HttpServletResponse res) {
        if (BaseUtils.isBlank(bucketName)){
            bucketName = prop.getBucketName();
        }
        if (!minioUtil.bucketExists(bucketName)){
            throw new CustomaizeExpetion(ExceptionEnum.NOT_FOUND);
        }
        minioUtil.download(fileName, res,bucketName);
        return Result.ok();
    }

    @ApiOperation(value = "删除文件", notes = "根据url地址删除文件")
    @PostMapping("/delete")
    public Result remove(String url,String bucketName) {
        if (BaseUtils.isBlank(bucketName)){
            bucketName = prop.getBucketName();
        }
        if (!minioUtil.bucketExists(bucketName)){
            throw new CustomaizeExpetion(ExceptionEnum.NOT_FOUND);
        }
        String objName = url.substring(url.lastIndexOf(bucketName + "/") + bucketName.length() + 1);
        minioUtil.remove(objName);
        return Result.ok(objName);
    }

    /**
     * 获取文件列表
     *
     * @param pageNum  页码
     * @param pageSize 一页的数量
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/fileList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Result<List<FileVo>> getFileList(Integer pageNum, Integer pageSize) throws Exception {
        return Result.ok(minioUtil.getFileList(pageNum, pageSize));
    }

}