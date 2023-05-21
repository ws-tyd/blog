package com.tyd.common.filter;

import com.alibaba.druid.support.json.JSONUtils;
import com.tyd.common.util.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * 我拒绝访问处理程序
 *
 * @author 谭越东
 * @date 2022/09/27
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

        response.setContentType("application/json;charset=utf-8");
        response.setStatus(Optional.ofNullable(((Integer) request.getAttribute("code"))).orElse(500));
        response.getWriter().write(JSONUtils.toJSONString(Result.fail(Optional.ofNullable(((Integer) request.getAttribute("code"))).orElse(500),
                Optional.ofNullable(((String) request.getAttribute("message"))).orElse("请联系网站管理员"))));
    }
}
