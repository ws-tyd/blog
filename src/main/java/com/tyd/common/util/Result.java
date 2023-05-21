package com.tyd.common.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.tyd.common.exception.ExceptionEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@NoArgsConstructor
public class Result<T> implements Serializable  {

    private static final long serialVersionUID = 42L;

    private Integer code = 200;
    private String message = "success";
    private Boolean status = true;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time = new Date();
    private T data;


    public static <T>Result<T> ok(){
        return new Result(200,"success",true,null);
    }
    public static <T>Result<T> ok(T data){
        return new Result(200,"success",true,data);
    }
    public static <T>Result<T> okMag(String message){
        return new Result(200,message,true,null);
    }
    public static <T>Result<T> ok(Integer code ,T data){
        return new Result(code,"success",true,data);
    }
    public static <T>Result<T> ok(Integer code,String message ,T data){
        return new Result(code,message,true,data);
    }
    public Result(Integer code, String message, Boolean status,T data){
        this.code=code;
        this.message=message;
        this.status=status;
        this.data=data;
    }
    public static <T>Result<T> fail(){
        return new Result(200,"fail",false,null);
    }
    public static <T>Result<T> failMsg(String message){
        return new Result(200,message,false,null);
    }
    public static <T>Result<T> fail(Integer code){
        return new Result(code,"fail",false,null);
    }
    public static <T>Result<T> fail(Integer code,String message){
        return new Result(code,message,false,null);
    }
    public static <T>Result<T> fail(ExceptionEnum exceptionEnum){
        return new Result(exceptionEnum.code,exceptionEnum.message,false,null);
    }
 }
