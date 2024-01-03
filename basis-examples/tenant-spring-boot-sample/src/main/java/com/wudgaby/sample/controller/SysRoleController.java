package com.wudgaby.sample.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.sample.domain.SysRole;
import com.wudgaby.sample.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/sysRoles")
@RequiredArgsConstructor
public class SysRoleController {
    private final SysRoleService roleService;

    @ApiOperation("角色分页列表")
    @GetMapping("/page")
    public ApiPageResult<SysRole> rolePageList(PageForm pageForm){
        return ApiPageResult.success(roleService.page(
                new Page<>(pageForm.getPageNum(), pageForm.getPageSize()),
                Wrappers.<SysRole>lambdaQuery().orderByDesc(SysRole::getCreateTime)));
    }

    @ApiOperation("角色列表")
    @GetMapping
    public ApiResult<List<SysRole>> roleList(){
        return ApiResult.success(roleService.list());
    }

    @ApiOperation("添加角色")
    @PostMapping
    public ApiResult addRole(@Validated @RequestBody SysRole sysRole){
        roleService.save(sysRole);
        return ApiResult.success();
    }

    @ApiOperation("更新角色")
    @PutMapping
    public ApiResult updateRole(@Validated @RequestBody SysRole sysRole){
        sysRole.setRoleCode(null);
        roleService.updateById(sysRole);
        return ApiResult.success();
    }

    @ApiOperation("删除角色")
    @DeleteMapping
    public ApiResult delRole(@RequestParam Long roleId){
        SysRole sysRole = roleService.getById(roleId);

        AssertUtil.notNull(sysRole, "角色不存在");

        roleService.removeById(roleId);
        return ApiResult.success();
    }

}
