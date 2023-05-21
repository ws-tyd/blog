package com.tyd.module.pojo.Vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class LoginVo implements Serializable {


    @NotEmpty(message = "账号不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;
}
