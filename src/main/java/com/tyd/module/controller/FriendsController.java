package com.tyd.module.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyd.common.exception.CustomaizeExpetion;
import com.tyd.common.exception.ExceptionEnum;
import com.tyd.common.util.Result;
import com.tyd.common.util.SecurityContextHandlerUtil;
import com.tyd.module.pojo.Friends;
import com.tyd.module.pojo.Param.FriendsParam;
import com.tyd.module.pojo.User;
import com.tyd.module.service.FriendsService;
import com.tyd.module.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/friends")
public class FriendsController {

    private final FriendsService friendsService;
    private final UserService userService;

    public FriendsController(FriendsService friendsService, UserService userService) {
        this.friendsService = friendsService;
        this.userService = userService;
    }

    @PostMapping("/add")
    @ApiOperation("添加关注")
    @PreAuthorize("hasAuthority('user') or hasAuthority('admin')")
    public Result add(@RequestBody @Valid FriendsParam param){
        User user = SecurityContextHandlerUtil.getUser();
        Friends fr = Friends.builder()
                .myId(user.getId())
                .friendsId(param.getId())
                .addTime(new Date())
                .build();
        friendsService.save(fr);
        return Result.ok("添加成功");
    }
    @PostMapping("/cancelFriends")
    @ApiOperation("取消关注")
    @PreAuthorize("hasAuthority('user') or hasAuthority('admin')")
    public Result delete(@RequestBody @Valid FriendsParam param){
        User user = SecurityContextHandlerUtil.getUser();
        if (friendsService.check(param)){
            friendsService.remove(Wrappers.<Friends>lambdaQuery().eq(Friends::getMyId, user.getId()).eq(Friends::getFriendsId, param.getId()));
            return Result.ok("取消成功");
        }
        return Result.ok("已取消");
    }
    @PostMapping("/isMyFlow")
    @ApiOperation("是否关注")
    @PreAuthorize("hasAuthority('user') or hasAuthority('admin')")
    public Result check(@RequestBody @Valid FriendsParam param){
        return Result.ok(friendsService.check(param));
    }

    @GetMapping("/fanList")
    @ApiOperation("粉丝列表")
    @PreAuthorize("hasAuthority('user') or hasAuthority('admin')")
    public Result fanList(){
        return Result.ok(friendsService.queryFanList("fan"));
    }

    @GetMapping("/followList")
    @ApiOperation("关注列表")
    @PreAuthorize("hasAuthority('user') or hasAuthority('admin')")
    public Result followList(){
        return Result.ok(friendsService.queryFanList("follow"));
    }
    @GetMapping("/queryFriendInIds/{ids}")
    @PreAuthorize("hasAuthority('user') or hasAuthority('admin')")
    public Result queryFriendInIds(@PathVariable String ids){
        String[] id = ids.split(",");
        if (id.length<1){
            throw new CustomaizeExpetion(ExceptionEnum.FAIL_RME_PARAM);
        }
        return Result.ok(userService.list(Wrappers.<User>lambdaQuery().in(User::getId,id)));
    }
}
