package com.tyd.module.pojo.Vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RepeatPassVo implements Serializable {
    private String oldPassword;
    private String newPassword;
}
