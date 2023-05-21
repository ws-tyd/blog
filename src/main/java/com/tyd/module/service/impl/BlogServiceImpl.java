package com.tyd.module.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tyd.common.exception.CustomaizeExpetion;
import com.tyd.common.exception.ExceptionEnum;
import com.tyd.common.util.Result;
import com.tyd.common.util.SecurityContextHandlerUtil;
import com.tyd.module.mapper.BlogMapper;
import com.tyd.module.mapper.TypeMapper;
import com.tyd.module.pojo.Blog;
import com.tyd.module.pojo.Type;
import com.tyd.module.pojo.User;
import com.tyd.module.service.BlogService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    private final BlogMapper blogMapper;

    private final TypeMapper typeMapper;

    public BlogServiceImpl(BlogMapper blogMapper, TypeMapper typeMapper) {
        this.blogMapper = blogMapper;
        this.typeMapper = typeMapper;
    }

    @Override
    public Result createBlog(Blog blog) {
        Type type = typeMapper.selectById(blog.getTypeId());
        if (type == null) {
            throw new CustomaizeExpetion(ExceptionEnum.TYPE_ERROR);
        }
        User user = SecurityContextHandlerUtil.getUser();
        blog.setCreateTime(new Date());
        blog.setAuthor(user.getUsername());
        blog.setUserId(user.getId());
        if (blogMapper.insert(blog) == 1) {
            return Result.ok();
        }
        throw new CustomaizeExpetion(ExceptionEnum.OPERATION_FAILED);
    }
}
