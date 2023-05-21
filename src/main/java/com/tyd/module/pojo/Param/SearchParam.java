package com.tyd.module.pojo.Param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchParam implements Serializable {
    private String key;
    private String orderBy;
    private int page;
    private int size;
    private String desc;
}
