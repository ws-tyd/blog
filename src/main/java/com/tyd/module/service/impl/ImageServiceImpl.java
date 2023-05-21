package com.tyd.module.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tyd.common.util.BaseUtils;
import com.tyd.common.util.SecurityContextHandlerUtil;
import com.tyd.module.mapper.ImageMapper;
import com.tyd.module.pojo.Image;
import com.tyd.module.pojo.User;
import com.tyd.module.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

    private final ImageMapper imageMapper;


    public ImageServiceImpl(ImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

    @Override
    public boolean saveImage(String endpoint, String bucketName, String objectName) {
        User user = SecurityContextHandlerUtil.getUser();
        Image image = Image.builder()
                .path(objectName)
                .picName(BaseUtils.getLastFile(objectName))
                .picUrl(endpoint + "/" + bucketName + "/" + objectName)
                .createTime(new Date())
                .bucketName(bucketName)
                .build();
        if (user != null){
            image.setAuthor(user.getUsername());
            image.setCreateUser(user.getId());
        }
        return imageMapper.insert(image) == 1;

    }
}
