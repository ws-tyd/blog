package com.tyd.module.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyd.common.util.*;
import com.tyd.module.pojo.Image;
import com.tyd.module.pojo.Vo.PageVo;
import com.tyd.module.service.ImageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.sql.Wrapper;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequestMapping("/image")
@RestController
public class ImageController {

    
    private final ImageService imageService;
    private final RedisUtils redisUtils;

    public ImageController(ImageService imageService, RedisUtils redisUtils) {
        this.imageService = imageService;
        this.redisUtils = redisUtils;
    }


//    @PostMapping("/upload")
//    public Result upload(HttpServletRequest request, MultipartFile file){
//        MyFileUtil.save(file);
//        return Result.ok();
//    }

    //添加
//    @PostMapping("/add")
//    public Result<Image> add(@Valid @RequestBody  Image  Image){
//         imageService.save( Image);
//        return  Result.ok( Image);
//    }
    //删除
//    @DeleteMapping("/delete/{id}")
//    public Result< Image> delete(@PathVariable("id")String id){
//         Image target =  imageService.getById(id);
//        if (null==target){
//            throw new CustomaizeExpetion(404,"没有找到"+id);
//        }
//        imageService.removeById(id);
//        return  Result.ok(target);
//    }
    //修改
    @PutMapping("/update")
    public Result< Image> update(@Valid @RequestBody  Image  Image){
        imageService.updateById( Image);
        return  Result.ok( Image);
    }
    //查询所有
    @GetMapping("/queryAll")
    public Result queryAll(){
        if (redisUtils.hasKey("imageListAll")) {
            JSONObject jsonObject = JSON.parseObject(redisUtils.get("imageListAll"));
            return Result.ok(jsonObject.getObject("list",Object.class));
        }
        List<Image> imageList = imageService.list();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",imageList);
        redisUtils.set("imageListAll",jsonObject.toJSONString(),1L, TimeUnit.DAYS);
        return  Result.ok(imageList);
    }
    //查询所有分页
    @PostMapping("/queryAll")
    public Result queryAllPage(@Valid @RequestBody PageVo pageVo){
        if (redisUtils.hasKey(pageVo.toString())) {
            JSONObject jsonObject = JSON.parseObject(redisUtils.get(pageVo.toString()));
            return Result.ok(jsonObject.getObject("list",Object.class));
        }
        PageUtil<Image> imageList = imageService.page(BaseUtils.initPageUtil(pageVo));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",imageList);
        redisUtils.set(pageVo.toString(),jsonObject.toJSONString(),1L, TimeUnit.DAYS);
        return  Result.ok(imageList);
    }
    //根据id查询
    @GetMapping("/query/{id}")
    public Result< Image> queryAllPage(@PathVariable("id") String id){
        return  Result.ok( imageService.getById(id));
    }


    @ApiOperation("查询该桶下的所有图片")
    @GetMapping("/querySliderVerifyPic/{bucketName}")
//    @Cacheable(cacheNames="SliderVerifyPic")
    public Result< List<Image> > querySliderVerifyPic(@PathVariable String bucketName){
        return  Result.ok(imageService.list(Wrappers.<Image>lambdaQuery().eq(Image::getBucketName, bucketName)));
    }
}
