package com.tyd.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tyd.module.pojo.Vo.PageVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 页面跑龙套
 *
 * @author 谭越东
 * @date 2022/10/27
 */
@Data
@AllArgsConstructor
public class PageUtil<T> implements IPage {
    protected List<T> records;
    protected long total;
    protected long size;
    protected long current;
    @JsonIgnore
    protected List<OrderItem> orders;
    @JsonIgnore
    protected boolean optimizeCountSql;
    @JsonIgnore
    protected boolean isSearchCount;
    @JsonIgnore
    protected boolean hitCount;
    @JsonIgnore
    protected String countId;
    @JsonIgnore
    protected Long maxLimit;

    public PageUtil(Integer current ,Integer size){
        this.current = current;
        this.size = size;
        this.records = Collections.emptyList();
        this.total = 0L;
        this.orders = new ArrayList();
        this.optimizeCountSql = true;
        this.isSearchCount = true;
        this.hitCount = false;
    }
    public PageUtil(){
        this.records = Collections.emptyList();
        this.total = 0L;
        this.size = 10L;
        this.current = 1L;
        this.orders = new ArrayList();
        this.optimizeCountSql = true;
        this.isSearchCount = true;
        this.hitCount = false;
    }
    public PageUtil(PageVo pageVo){
        this.current = pageVo.getCurrent();
        this.size = pageVo.getSize();
        this.records = Collections.emptyList();
        this.total = 0L;
        this.orders = new ArrayList();
        this.optimizeCountSql = true;
        this.isSearchCount = true;
        this.hitCount = false;
    }
    @Override
    public List<OrderItem> orders() {
        return this.orders;
    }

    @Override
    public List getRecords() {
        return this.records;
    }

    @Override
    public IPage setRecords(List records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return this.total;
    }

    @Override
    public IPage setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public IPage setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return this.current;
    }

    @Override
    public IPage setCurrent(long current) {
        this.current = current;
        return this;
    }
}
