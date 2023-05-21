package com.tyd.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum ExceptionEnum {

    BASE_FAIL(500,"请联系网站管理员解决问题"),
    NOT_FOUND(404,"资源未找到"),
    FAIL_RME(403,"请求方法错误"),
    FAIL_RME_PARAM(406,"请求参数错误"),
    FAIL_TOKEN(401,"token失效"),
    FAIL_DATADUPLICATION(405,"你输入的数据重复，请更换之后在重试"),
    FAIL_CONSTRAINTERROR(405,"数据约束错误,请联系网站负责人处理"),
    VERIFICATION_FAILED(401,"验证失败"),
    OPERATION_FAILED(400,"操作失败"),
    LOGIN_FAIL(400,"登录失败"),
    TYPE_ERROR(404,"没有这个板块"),
    ACCESS_DENIEDEXCEPTION(403, "权限不足，不允许访问");


    public Integer code;

    public String message;


}
