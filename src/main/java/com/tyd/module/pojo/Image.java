package com.tyd.module.pojo;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("b_image")
@Builder
public class Image implements Serializable {
    private Integer id;
    private String path;
    private String picName;
    private String picUrl;
    private Date createTime;
    private Date updateTime;
    private String picType;
    private Integer createUser;
    private String author;
    private String bucketName;


//    @Data
//    static class build {
//        private Integer id;
//        private String path;
//        private String picName;
//        private String picUrl;
//        private Date createTime;
//        private Date updateTime;
//        private String picType;
//        private Integer createUser;
//        private String author;
//        private String bucketName;
//        public Image build() {
//            return new Image(this);
//        }
//
//    }
//
//    public Image(build build) {
//        id = build.id;
//        path = build.path;
//        picName = build.picName;
//        picUrl = build.picUrl;
//        createTime = build.createTime;
//        updateTime = build.updateTime;
//        picType = build.picType;
//        createUser = build.createUser;
//        author = build.author;
//        bucketName = build.bucketName;
//    }
}
