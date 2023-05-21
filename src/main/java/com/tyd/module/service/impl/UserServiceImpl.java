package com.tyd.module.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tyd.common.constant.FwConstants;
import com.tyd.common.exception.CustomaizeExpetion;
import com.tyd.common.exception.ExceptionEnum;
import com.tyd.common.util.*;
import com.tyd.module.mapper.UserMapper;
import com.tyd.module.pojo.User;
import com.tyd.module.pojo.Vo.LoginVo;
import com.tyd.module.pojo.Vo.MyUserDetails;
import com.tyd.module.pojo.Vo.RepeatPassVo;
import com.tyd.module.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    private final UserMapper userMapper;

    private final RedisUtils redisUtils;

    @Value("${jwt.expirationTime}")
    private long expirationTime;

    public UserServiceImpl(UserMapper userMapper, RedisUtils redisUtils) {
        this.userMapper = userMapper;
        this.redisUtils = redisUtils;
    }

    //获取认证信息
    @Override
    public Authentication getAuthentication(String username){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User target = userMapper.selectOne(wrapper);
        MyUserDetails user = new MyUserDetails();
        user.setUser(target);
        user.setUsername(username);
        user.setPassword(target.getPassword());
        HashSet<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(target.getRole()));
        user.setAuthorities(authorities);
        return new UsernamePasswordAuthenticationToken(user,user.getUsername(),user.getAuthorities());
    }

    @Override
    public Result login(LoginVo user, HttpServletRequest request) {

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",user.getUsername()).or()
                .eq("phone",user.getUsername()).or()
                .eq("user_id",user.getUsername()).or()
                .eq("email",user.getUsername());
        User target = userMapper.selectOne(wrapper);
        if (target != null && user.getPassword().equals(target.getPassword())){
            String token = JwtUtil.buildToken(target);
            redisUtils.set(FwConstants.prefixToken+token,token,expirationTime, TimeUnit.DAYS);
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(target.getUsername()));
            log.info(user.getUsername()+"登陆成功    "+"登录时间 ："+new Date());
            HashMap<String, Object> data = new HashMap<>();
            data.put("token",token);
            data.put("user",target);
            return Result.ok(200,"登录成功",data);
        }
        log.error(user.getUsername()+"登陆失败    "+"登录时间 ："+ BaseUtils.timeFormat(new Date()));
        throw new CustomaizeExpetion(ExceptionEnum.LOGIN_FAIL);
    }

    @Override
    public Result register(User user, HttpServletRequest request,String role) {
        user.setUserId(BaseUtils.generateUserId());
        user.initRole(role);
        user.setCreateTime(new Date());
        userMapper.insert(user);
        return Result.ok(200,"注册成功",user);
    }

    @Override
    public Result repeatPass(RepeatPassVo repeatPass) {
        User user = SecurityContextHandlerUtil.getUser();
        if (repeatPass.getOldPassword().equals(user.getPassword())){
            user.setPassword(repeatPass.getNewPassword());
            userMapper.updateById(user);
        }else {
            throw new CustomaizeExpetion(200,"旧密码不对,请重试");
        }
        return Result.ok();
    }


    //清楚token记录
//    public Result clearToken(){
//
//    }
}
