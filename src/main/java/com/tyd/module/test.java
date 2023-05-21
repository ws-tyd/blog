package com.tyd.module;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.tyd.common.util.BaseUtils;
import com.tyd.common.util.Result;
import com.tyd.module.pojo.TestVo;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {

    @PostMapping("/test")
    @SneakyThrows
    public Result test(@RequestBody TestVo testVo){
        System.out.println(testVo);
        System.out.println(BaseUtils.toJsonStr(testVo));
        System.out.println(JSON.toJSONString(testVo));
        return Result.ok(testVo);
    }
}
