package com.tyd.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyd.module.pojo.Friends;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface FriendsMapper extends BaseMapper<Friends> {


    //查询粉丝列表
    @Select("select my_id from b_friends where friends_id = #{id}")
    List<Integer> queryFanList(@Param("id") Integer id);

    //查询关注列表
    @Select("select friends_id from b_friends where  my_id = #{id}")
    List<Integer> queryFollowList(@Param("id") Integer id);

}
