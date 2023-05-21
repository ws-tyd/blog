package com.tyd.module.pojo;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("b_type")
public class Type implements Serializable {

  private Integer id;
  @NotEmpty(message = "板块名不能为空")
  private String typeName;
  private Integer deleteFlag;
  private Date createTime;
  private Date updateTime;
  private Integer createUser;
  private Integer updateUser;
  private String typeInfo;
  private String pic;



}
