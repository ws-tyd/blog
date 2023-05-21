package com.tyd.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyd.module.pojo.Blog;
import com.tyd.module.pojo.Type;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
}
