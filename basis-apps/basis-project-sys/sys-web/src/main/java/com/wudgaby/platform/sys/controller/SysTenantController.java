package com.wudgaby.platform.sys.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sys.entity.SysTenant;
import com.wudgaby.platform.sys.service.SysTenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 租户 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Api(tags = "租户管理")
@RestController
@RequestMapping("/sysTenants")
@RequiredArgsConstructor
public class SysTenantController {
    private final SysTenantService sysTenantService;

    @ApiOperation("租户分页列表")
    @GetMapping("/page")
    public ApiPageResult<SysTenant> pageList(PageForm pageForm){
        return ApiPageResult.success(sysTenantService.page(
                new Page<>(pageForm.getPageNum(), pageForm.getPageSize()),
                Wrappers.<SysTenant>lambdaQuery().orderByDesc(SysTenant::getCreateTime)));
    }

    @ApiOperation("租户列表")
    @GetMapping
    public ApiResult<List<SysTenant>> list(){
        return ApiResult.success(sysTenantService.list());
    }

    @ApiOperation("添加租户")
    @PostMapping
    public ApiResult addTenant(@Validated @RequestBody SysTenant sysTenant){
        sysTenantService.save(sysTenant);
        return ApiResult.success();
    }

    @ApiOperation("更新租户")
    @PutMapping
    public ApiResult updateTenant(@Validated @RequestBody SysTenant sysTenant){
        sysTenantService.updateById(sysTenant);
        return ApiResult.success();
    }

    @ApiOperation("删除租户")
    @DeleteMapping
    public ApiResult delTenant(@RequestParam Long id){
        sysTenantService.removeById(id);
        return ApiResult.success();
    }
}
