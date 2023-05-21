package com.tyd.module.controller;

import com.tyd.common.constant.FwConstants;
import com.tyd.common.exception.CustomaizeExpetion;
import com.tyd.common.exception.ExceptionEnum;
import com.tyd.common.util.BaseUtils;
import com.tyd.common.util.PageUtil;
import com.tyd.common.util.Result;
import com.tyd.module.pojo.User;
import com.tyd.module.pojo.Vo.LoginVo;
import com.tyd.module.pojo.Vo.PageVo;
import com.tyd.module.pojo.Vo.RepeatPassVo;
import com.tyd.module.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result login(@RequestBody @Valid LoginVo user, HttpServletRequest request){
        return userService.login(user,request);
    }
    @PostMapping("/register/{role}")
    @ApiOperation("用户注册")
    public Result registerCustom(@RequestBody @Valid User user, HttpServletRequest request, @PathVariable("role")String role){
        return userService.register(user,request,role);
    }
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result register(@RequestBody @Valid User user, HttpServletRequest request){
        return userService.register(user,request,"user");
    }
    @PostMapping("/repeatPass")
    @ApiOperation("用户修改密码")
    public Result repeatPass(@RequestBody @Valid RepeatPassVo repeatPass){
        return userService.repeatPass(repeatPass);
    }

    @PostMapping("/repeatUser")
    @ApiOperation("修改个人信息")
    public Result repeatUser(@RequestBody @Valid User user){
        if (userService.updateById(user)) {
            return Result.ok();
        }else {
          throw new CustomaizeExpetion(ExceptionEnum.OPERATION_FAILED);
        }
    }
    @GetMapping("/queryById/{id}")
    @ApiOperation("根据id查询用户信息")
    public Result queryById(@PathVariable("id")Integer id){
        return Result.ok(userService.getById(id));
    }
    @GetMapping("/queryAll")
    @ApiOperation("查询用户信息列表")
    public Result queryAll(){
        return Result.ok(userService.list());
    }
    @PostMapping("/queryAll")
    @ApiOperation("查询用户信息列表")
    public Result queryAllPage(@RequestBody @Valid PageVo pageVo){
        PageUtil<User> pageUtil = BaseUtils.initPageUtil(pageVo);
//        PageUtil<User> pageUtil1 = new PageUtil<>(pageVo);
        return Result.ok(userService.page(pageUtil));
    }
}
