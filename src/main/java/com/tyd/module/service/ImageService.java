package com.tyd.module.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tyd.module.pojo.Image;

public interface ImageService extends IService<Image> {


    boolean saveImage(String endpoint, String bucketName, String objectName);
}
