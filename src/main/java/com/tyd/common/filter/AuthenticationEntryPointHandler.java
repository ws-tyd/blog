package com.tyd.common.filter;

import com.alibaba.druid.support.json.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyd.common.util.BaseUtils;
import com.tyd.common.util.Result;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证入口点处理程序
 *
 * @author 谭越东
 * @date 2022/09/27
 */
@Component
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.setStatus(403);
        httpServletResponse.getWriter().write(BaseUtils.toJsonStr(Result.fail(403,"身份验证失败")));
    }
}
