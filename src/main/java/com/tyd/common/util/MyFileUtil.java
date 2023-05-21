package com.tyd.common.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Random;

@Slf4j
public class MyFileUtil{

    private static String path = "E:\\javaWeb\\blog\\pic\\";

    @Value("${file.path}")
    private String url;

    @PostConstruct
    public void initPath() {
        path = this.url;
    }

    /**
     * 获得新文件名
     *
     * @param oldName 旧名称
     * @return {@link String}
     */
    public static String getNewFilename(String oldName) {
        String suffix = BaseUtils.extName(oldName);
        return System.currentTimeMillis() +"_"+ new Random().nextInt(10000)  + suffix;
    }

    public static void save(MultipartFile file){
        File dir = new File(path);
        if (!dir.isDirectory()){
            dir.mkdir();
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        //创建管道
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        String filename = getNewFilename(file.getOriginalFilename());
        String outputPath = path+filename;
        log.info("文件输出位置{}",outputPath);
        try {
            fis = (FileInputStream) file.getInputStream();
            fos = new FileOutputStream(outputPath);

            //读取channel
            inChannel = fis.getChannel();
            outChannel = fos.getChannel();

            //通道间传输数据
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }finally {
            if (inChannel != null){
                try {
                    inChannel.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if(outChannel != null){
                try {
                    outChannel.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if (fis != null){
                try {
                    fis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if(fos != null){
                try {
                    fos.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
