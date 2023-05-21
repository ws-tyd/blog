package com.tyd.module.pojo.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class FileVo implements Serializable {
    private String bucketName;    //  文件夹
    private String fileName;    // 文件名
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;   // 最后修改时间
    private String fileSize;   //  文件大小

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
