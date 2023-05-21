package com.tyd.module.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyd.common.util.RedisUtils;
import com.tyd.common.util.Result;
import com.tyd.module.pojo.Param.SearchParam;
import com.tyd.module.pojo.User;
import com.tyd.module.service.BlogService;
import com.tyd.module.service.TypeService;
import com.tyd.module.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final BlogService blogService;
    private final UserService userService;
    private final TypeService typeService;
    private final RedisUtils redisUtils;

    public SearchController(BlogService blogService, UserService userService, TypeService typeService, RedisUtils redisUtils) {
        this.blogService = blogService;
        this.userService = userService;
        this.typeService = typeService;
        this.redisUtils = redisUtils;
    }

    @PostMapping("/user")
    public Result searchUser(@RequestBody SearchParam param){
        String key = param.getKey();
        String[] keys = key.split("");
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        for (String s : keys) {
            wrapper.like(User::getUsername,s).or();
        }
       return Result.ok(userService.list(wrapper));
    }
}
