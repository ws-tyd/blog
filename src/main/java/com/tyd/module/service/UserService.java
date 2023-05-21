package com.tyd.module.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tyd.common.util.Result;
import com.tyd.module.pojo.User;
import com.tyd.module.pojo.Vo.LoginVo;
import com.tyd.module.pojo.Vo.RepeatPassVo;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {


    //获取认证信息
    Authentication getAuthentication(String username);

    Result login(LoginVo user, HttpServletRequest request);

    Result register(User user, HttpServletRequest request,String role);

    Result repeatPass(RepeatPassVo repeatPass);
}
