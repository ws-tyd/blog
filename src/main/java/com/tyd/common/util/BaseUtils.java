package com.tyd.common.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.format.DataFormatDetector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyd.module.pojo.User;
import com.tyd.module.pojo.Vo.PageVo;
import javafx.scene.input.DataFormat;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.function.Supplier;

/**
 * 基础的攻击类
 *
 * @author 谭越东
 * @date 2022/09/27
 */
@SuppressWarnings("all")
public class BaseUtils {

    /**
     * 时间格式
     *
     * @param date 日期
     * @return {@link String}
     *///格式化时间
    public static String timeFormat(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    /**
     * 时间格式
     *
     * @param date 日期
     * @return {@link String}
     *///格式化时间
    public static String timeFormat(Date date,String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
    /**
     * 检查excel文件
     *
     * @param file 文件
     * @return boolean
     *///判断文件类型
    public static boolean checkExcelFile(MultipartFile file){
        String filename = file.getOriginalFilename();
        if (filename!=null&&!filename.contains(".xlsx") && !filename.contains(".xls")) {
            return true;
        }
        return false;
    }

    /**
     * 垫了0
     *
     * @param inputString 输入字符串
     * @param length      长度
     * @return {@link String}
     */
    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);
        return sb.toString();
    }

    /**
     * 生成没有下滑线的UUID
     *
     * @return {@link String}
     */
    public static String noUnderscoreUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

    /**
     * 转换为josn
     *
     * @param o o
     * @return {@link String}
     * @throws JsonProcessingException json处理异常
     */
    public static String toJsonStr(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }

    /**
     * 生成用户id
     *
     * @return {@link Long}
     */
    public static Long generateUserId(){
        SnowFlake snowFlake = new SnowFlake(1, 1);
        Supplier<Long> supplier = ()->snowFlake.nextId();
        return supplier.get();
    }


    /**
     * 初始化页面跑龙套
     *
     * @param pageVo 页签证官
     * @return {@link PageUtil}
     */
    public static PageUtil initPageUtil(PageVo pageVo){
        return new PageUtil<>(pageVo);
    }

    /**
     * 获取文件后缀
     *
     * @param oldName 旧名称
     * @return {@link String}
     */
    public static String extName(String oldName) {
        return oldName.substring(oldName.lastIndexOf('.'));
    }


    /**
     * 是空白
     *
     * @param str str
     * @return boolean
     */
    public static boolean isBlank(String str){
        return (str==null||str.trim().equals(""));
    }

    /**
     * 得到最后一个文件
     *
     * @param str str
     * @return {@link String}
     */
    public static String getLastFile(String str){
        int index = str.lastIndexOf('/');
        return str.substring(index == -1 ?0:index);
    }

    public static void main(String[] args) {
        System.out.println("-------------------");
        System.out.println(generateUserId());
        System.out.println("-------------------");
        System.out.println(getLastFile("http://localhost:8080/limi/swagger-ui.html#/%E6%96%87%E4%BB%B6%E7%9B%B8%E5%85%B3%E6%8E%A5%E5%8F%A3/uploadUsingPOST"));
    }


}
