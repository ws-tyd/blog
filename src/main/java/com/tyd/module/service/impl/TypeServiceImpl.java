package com.tyd.module.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tyd.module.mapper.TypeMapper;
import com.tyd.module.pojo.Type;
import com.tyd.module.service.TypeService;
import org.springframework.stereotype.Service;

@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {


}
