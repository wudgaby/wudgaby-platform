package com.wudgaby.platform.sys.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sys.entity.SysDictType;
import com.wudgaby.platform.sys.form.DictTypeForm;
import com.wudgaby.platform.sys.service.SysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 字典类型表 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Api(tags = "字典类型管理")
@RestController
@RequestMapping("/dictTypes")
@AllArgsConstructor
public class SysDictTypeController {
    private final SysDictTypeService sysDictTypeService;

    @ApiOperation("字典类型列表")
    @GetMapping
    public ApiPageResult<SysDictType> list(PageForm pageForm){
        return ApiPageResult.success(sysDictTypeService.page(
                new Page<>(pageForm.getPageNum(), pageForm.getPageSize()),
                Wrappers.<SysDictType>lambdaQuery().orderByDesc(SysDictType::getCreateTime)));
    }

    @ApiOperation("添加字典类型")
    @PostMapping
    public ApiResult add(@Validated @RequestBody DictTypeForm dictTypeForm){
        sysDictTypeService.addDictType(dictTypeForm);
        return ApiResult.success();
    }

    @ApiOperation("更新字典类型")
    @PutMapping
    public ApiResult update(@Validated @RequestBody DictTypeForm dictTypeForm){
        sysDictTypeService.updateDictType(dictTypeForm);
        return ApiResult.success();
    }

    @ApiOperation("删除字典类型")
    @DeleteMapping
    public ApiResult delete(@RequestBody List<Long> ids){
        sysDictTypeService.delDictType(ids);
        return ApiResult.success();
    }
}
