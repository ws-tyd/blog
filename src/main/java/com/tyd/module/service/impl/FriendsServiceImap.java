package com.tyd.module.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tyd.common.util.Result;
import com.tyd.common.util.SecurityContextHandlerUtil;
import com.tyd.module.mapper.FriendsMapper;
import com.tyd.module.pojo.Friends;
import com.tyd.module.pojo.Param.FriendsParam;
import com.tyd.module.pojo.User;
import com.tyd.module.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Service
public class FriendsServiceImap extends ServiceImpl<FriendsMapper, Friends> implements FriendsService {


    private final FriendsMapper friendsMapper;

    public FriendsServiceImap(FriendsMapper friendsMapper) {
        this.friendsMapper = friendsMapper;
    }

    @Override
    public boolean check(@RequestBody @Valid FriendsParam param){
        User user = SecurityContextHandlerUtil.getUser();
        Friends friends = friendsMapper.selectOne(Wrappers.<Friends>lambdaQuery().eq(Friends::getMyId, user.getId()).eq(Friends::getFriendsId, param.getId()));
        if (ObjectUtils.isEmpty(friends)){
            return false;
        }
        return true;
    }

    @Override
    public List<Integer> queryFanList(String type) {
        User user = SecurityContextHandlerUtil.getUser();
        return type.equals("fan")?friendsMapper.queryFanList(user.getId()):friendsMapper.queryFollowList(user.getId());
    }
}
