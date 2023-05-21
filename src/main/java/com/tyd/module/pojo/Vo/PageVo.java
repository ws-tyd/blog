package com.tyd.module.pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo implements Serializable {
    @Range(min = 1,message = "分页范围有误")
    private Integer current = 1;
    @Range(min = 1,max = 1000,message = "分页范围有误")
    private Integer size = 10;
    private String desc = null;
}
