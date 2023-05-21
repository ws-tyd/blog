package com.tyd.module.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("b_friends")
public class Friends {

  @TableId
  private int id;
  private int myId;
  private int friendsId;
  private Date addTime;
  @TableField("`from`")
  private String from;
  private String remark;

}
