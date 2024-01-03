package com.wudgaby.sample.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.security.core.SecurityUtils;
import com.wudgaby.sample.domain.SysTenant;
import com.wudgaby.sample.service.SysTenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
    @ApiOperation("更改当前会话租户")
    @PostMapping("/changeTenant")
    public ApiResult changeTenant(@RequestParam Long tenantId, @ApiParam(hidden = true) HttpSession session){
        session.setAttribute(SecurityUtils.TENANT_KEY, tenantId);
        return ApiResult.success();
    }

    @ApiOperation("租户分页列表")
    @GetMapping("/page")
    public ApiPageResult<SysTenant> tenantPageList(PageForm pageForm){
        return ApiPageResult.success(sysTenantService.page(
                new Page<>(pageForm.getPageNum(), pageForm.getPageSize()),
                Wrappers.<SysTenant>lambdaQuery().orderByDesc(SysTenant::getCreateTime)));
    }

    @ApiOperation("租户列表")
    @GetMapping
    public ApiResult<List<SysTenant>> tenantList(){
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
