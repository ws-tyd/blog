package com.tyd.module.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tyd.common.util.Result;
import com.tyd.module.pojo.Blog;


public interface BlogService extends IService<Blog> {
    Result createBlog(Blog blog);
    //获取认证信息
}
