package com.tyd.module.pojo;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("b_blog")
public class Blog implements Serializable  {
  private Integer id;
  @NotEmpty(message = "标题不能为空")
  private String title;
  private String info;
  @NotNull(message = "板块id不能为空")
  private Integer typeId;
  @NotEmpty(message = "文章内容不能为空")
  private String text;
  @NotEmpty(message = "作者名不能为空")
  private String author;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;
  private Integer view;
  private Integer hits;
  private Integer postNum;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;
  private Integer isTop;
  private Integer hotWeights;
  private Integer deleteFlag;
  private Integer isCuration;
  private String label;
  private Integer starNum;
  private Integer stompNum;
  private Integer privacyLevel;
  @NotNull(message = "用户id不能为空")
  private Integer userId;

}
