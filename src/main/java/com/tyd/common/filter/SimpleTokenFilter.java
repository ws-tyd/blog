package com.tyd.common.filter;

import com.alibaba.druid.util.StringUtils;
import com.tyd.common.constant.FwConstants;
import com.tyd.common.util.BaseUtils;
import com.tyd.common.util.JwtUtil;
import com.tyd.common.util.RedisUtils;
import com.tyd.common.util.ip.AddressUtils;
import com.tyd.common.util.ip.IpUtils;
import com.tyd.module.pojo.User;
import com.tyd.module.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.AccessControlException;
import java.util.Date;

/**
 * 简单标记过滤器
 *
 * @author 谭越东
 * @date 2022/09/27
 */
@Component
@Slf4j
public class SimpleTokenFilter extends OncePerRequestFilter {



    private final RedisUtils redisUtils;

    private final UserService userService;

    public SimpleTokenFilter(RedisUtils redisUtils, UserService userService) {
        this.redisUtils = redisUtils;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        log.info("请求ip:"+ IpUtils.getIpAddr(request)+
                "请求位置信息:"+ AddressUtils.getRealAddressByIP(IpUtils.getIpAddr(request))+
                "请求的接口:"+request.getRequestURI()+
                "请求的方法:"+request.getMethod()+
                "请求时间："+ BaseUtils.timeFormat(new Date())+
                "认证信息："+authorization);
        if (!StringUtils.isEmpty(authorization) && authorization.contains("Bearer ")) {
            authorization = authorization.substring("Bearer ".length());
            if (redisUtils.hasKey(FwConstants.prefixToken + authorization)) {
                Authentication authentication = userService.getAuthentication(JwtUtil.getUserName(authorization));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else {
                request.setAttribute("code", 401);
                request.setAttribute("message", "token非法");
                throw new AccessControlException("token非法");
            }
        }
        filterChain.doFilter(request, response);
    }
}
