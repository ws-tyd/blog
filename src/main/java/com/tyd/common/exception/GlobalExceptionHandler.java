package com.tyd.common.exception;

import com.tyd.common.util.BaseUtils;
import com.tyd.common.util.Result;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;

/**
 * 全局异常处理程序
 *
 * @author 谭越东
 * @date 2022/09/27
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result baseExpetion(Exception e, HttpServletResponse response){
        e.printStackTrace();
        log.error(e.getMessage()+"    时间 :"+BaseUtils.timeFormat(new Date()));
        response.setStatus(ExceptionEnum.BASE_FAIL.code);
        return Result.fail(ExceptionEnum.BASE_FAIL);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletResponse response){
        log.error(e.getMessage()+"    时间 :"+BaseUtils.timeFormat(new Date()));
        response.setStatus(ExceptionEnum.FAIL_RME.code);
        return Result.fail(ExceptionEnum.FAIL_RME);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public Result ExpiredJwtException(HttpRequestMethodNotSupportedException e, HttpServletResponse response){
        log.error(e.getMessage()+"    时间 :"+BaseUtils.timeFormat(new Date()));
        response.setStatus(ExceptionEnum.FAIL_RME.code);
        return Result.fail(ExceptionEnum.FAIL_RME);
    }
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result SQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e, HttpServletResponse response){
        log.error(e.getMessage()+"    时间 :"+BaseUtils.timeFormat(new Date()));
        response.setStatus(ExceptionEnum.FAIL_CONSTRAINTERROR.code);
        return Result.fail(ExceptionEnum.FAIL_CONSTRAINTERROR);
    }
    @ExceptionHandler(DuplicateKeyException.class)
    public Result DuplicateKeyException(DuplicateKeyException e, HttpServletResponse response){
        log.error(e.getMessage()+"    时间 :"+BaseUtils.timeFormat(new Date()));
        response.setStatus(ExceptionEnum.FAIL_DATADUPLICATION.code);
        return Result.fail(ExceptionEnum.FAIL_DATADUPLICATION);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public Result AccessDeniedException(AccessDeniedException e, HttpServletResponse response){
        log.error(e.getMessage()+"    时间 :"+BaseUtils.timeFormat(new Date()));
        response.setStatus(ExceptionEnum.ACCESS_DENIEDEXCEPTION.code);
        return Result.fail(ExceptionEnum.ACCESS_DENIEDEXCEPTION);
    }
    @ExceptionHandler(CustomaizeExpetion.class)
    public Result CustomaizeExpetion(CustomaizeExpetion e, HttpServletResponse response){
        log.error(e.getMessage()+"    时间 :"+BaseUtils.timeFormat(new Date()));
        response.setStatus(e.getCode());
        return Result.fail(e.getCode(),e.getMessage());
    }
}
