package com.tyd.module.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tyd.module.pojo.Friends;
import com.tyd.module.pojo.Param.FriendsParam;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface FriendsService extends IService<Friends> {
    boolean check(@RequestBody @Valid FriendsParam param);

    List<Integer> queryFanList(String type);
}
