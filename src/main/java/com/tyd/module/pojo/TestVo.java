package com.tyd.module.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TestVo implements Serializable {
    @JSONField(name = "NAME")
    private String name;

    @JsonProperty("Sex")
    private String sex;


}


