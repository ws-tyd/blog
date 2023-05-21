package com.tyd.module.pojo;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("b_user")
public class User implements Serializable {

    private Integer id;
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String sex;
    private String role;
    private Date createTime;
    private Integer avatarId;
    private String avatarUrl;
    private String info;

    public void initRole(String type){
        if (type.equals("100")) {
            this.role = "vip";
        }else if (type.equals("999")){
            this.role = "admin";
        }else if (type.equals("9999")){
            this.role = "superAdmin";
        }else {
            this.role = "user";
        }
    }

}
