package com.tyd.common.filter;

import com.tyd.common.util.BaseUtils;
import com.tyd.common.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.AccessControlException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 请求计数过滤
 *
 * @author 谭越东
 * @date 2022/09/27
 */
@Component
@Slf4j
public class RequestCountFilter extends OncePerRequestFilter {

    private final RedisUtils redisUtils;

    @Value("${requestCountFilter.count}")
    private Integer num;

    public RequestCountFilter(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String reqId = request.getRequestURI()+request.getMethod()+request.getRemoteAddr();
        Integer count = 0;
        if (redisUtils.hasKey(reqId)){
             count = Integer.valueOf(redisUtils.get(reqId));
            if (count > num){
                log.error(reqId+"请求次数过多  累计请求超过"+count+"  时间："+ BaseUtils.timeFormat(new Date()));
                request.setAttribute("code",429);
                request.setAttribute("message","请求次数过多，请稍后再试");
                throw new AccessControlException("请求次数过多");
            }
        }
        redisUtils.set(reqId,String.valueOf(count+1),1, TimeUnit.MINUTES);
        filterChain.doFilter(request,response);
    }
}
