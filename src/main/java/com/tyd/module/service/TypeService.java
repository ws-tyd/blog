package com.tyd.module.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tyd.common.util.Result;
import com.tyd.module.pojo.Type;
import com.tyd.module.pojo.User;
import com.tyd.module.pojo.Vo.LoginVo;
import com.tyd.module.pojo.Vo.RepeatPassVo;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface TypeService extends IService<Type> {
    //获取认证信息
}
