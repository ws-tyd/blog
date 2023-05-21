package com.tyd.module.controller;

import com.tyd.common.exception.ExceptionEnum;
import com.tyd.common.util.BaseUtils;
import com.tyd.common.util.Result;
import com.tyd.module.pojo.Type;
import com.tyd.module.pojo.Vo.PageVo;
import com.tyd.module.service.TypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * 类型控制器
 *
 * @author 谭越东
 * @date 2022/10/06
 */
@RestController
@RequestMapping("/type")
public class TypeController {

    @Autowired
    private TypeService typeService;

    /**
     * 查询通过id
     *
     * @param id id
     * @return {@link Result}
     */
    @GetMapping("/queryById/{id}")
    @ApiOperation("根据id查询文章信息")
    public Result queryById(@PathVariable("id")Integer id){
        return Result.ok(typeService.getById(id));
    }

    /**
     * 查询所有
     *
     * @return {@link Result}
     */
    @GetMapping("/queryAll")
    @ApiOperation("查询文章信息列表")
    public Result queryAll(){
        return Result.ok(typeService.list());
    }

    /**
     * 查询所有页面
     *
     * @param pageVo 页签证官
     * @return {@link Result}
     */
    @PostMapping("/queryAll")
    @ApiOperation("查询文章信息列表")
    public Result queryAllPage(@RequestBody @Valid PageVo pageVo){
        return Result.ok(typeService.page(BaseUtils.initPageUtil(pageVo)));
    }

    /**
     * 创建类型
     *
     * @param type 类型
     * @return {@link Result}
     */
    @PostMapping("/createType")
    @ApiOperation("增加一个标签")
    @PreAuthorize("hasAuthority('superAdmin')")
    public Result createType(@RequestBody @Valid Type type){
        type.setCreateTime(new Date());
        return typeService.save(type)?Result.ok():Result.fail(ExceptionEnum.OPERATION_FAILED);
    }

    /**
     * 更新类型
     *
     * @param type 类型
     * @return {@link Result}
     */
    @PostMapping("/updateType")
    @ApiOperation("修改标签信息")
    @PreAuthorize("hasAuthority('superAdmin')")
    public Result updateType(@RequestBody @Valid Type type){
        type.setUpdateTime(new Date());
        return typeService.updateById(type)?Result.ok():Result.fail(ExceptionEnum.OPERATION_FAILED);
    }

    @PostMapping("/deleteType/{id}")
    @ApiOperation("删除标签信息")
    @PreAuthorize("hasAuthority('superAdmin')")
    public Result deleteType(@PathVariable("id") Integer id){
        return typeService.removeById(id)?Result.ok():Result.fail(ExceptionEnum.OPERATION_FAILED);
    }
}
