package com.tyd.module.controller;

import com.tyd.common.exception.ExceptionEnum;
import com.tyd.common.util.BaseUtils;
import com.tyd.common.util.PageUtil;
import com.tyd.common.util.Result;
import com.tyd.module.pojo.Blog;
import com.tyd.module.pojo.User;
import com.tyd.module.pojo.Vo.PageVo;
import com.tyd.module.service.BlogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/blog")
public class BlogController {

    private final BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/queryById/{id}")
    @ApiOperation("根据id查询博客信息")
    public Result queryById(@PathVariable("id")Integer id){
        return Result.ok(blogService.getById(id));
    }


    @GetMapping("/queryAll")
    @ApiOperation("查询博客列表")
    public Result queryAll(){
        return Result.ok(blogService.list());
    }


    @PostMapping("/queryAll")
    @ApiOperation("查询博客信息列表")
    public Result queryAllPage(@RequestBody @Valid PageVo pageVo){
        PageUtil<User> pageUtil = BaseUtils.initPageUtil(pageVo);
        return Result.ok(blogService.page(pageUtil));
    }


    @PostMapping("/createBlog")
    @ApiOperation("增加一个博客")
    @PreAuthorize("hasAuthority('user')")
    public Result createBlog(@RequestBody @Valid Blog blog){
        return blogService.createBlog(blog);
    }

    @PostMapping("/updateBlog")
    @ApiOperation("修改博客信息")
    @PreAuthorize("hasAuthority('user')")
    public Result updateBlog(@RequestBody @Valid Blog blog){
        blog.setUpdateTime(new Date());
        return blogService.updateById(blog)?Result.ok():Result.fail(ExceptionEnum.OPERATION_FAILED);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除博客信息")
    @PreAuthorize("hasAuthority('user')")
    public Result deleteBlog(@PathVariable("id") Integer id){
        return blogService.removeById(id)?Result.ok():Result.fail(ExceptionEnum.OPERATION_FAILED);
    }

    public static void main(String[] args) {

    }
}
