package com.wudgaby.platform.permission.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.permission.entity.*;
import com.wudgaby.platform.permission.service.BaseRoleService;
import com.wudgaby.platform.permission.service.BaseRoleUserService;
import com.wudgaby.platform.permission.vo.MenuForm;
import com.wudgaby.platform.permission.vo.RoleForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统角色-基础信息 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-17
 */
@Api(tags = "角色管理")
@AllArgsConstructor
@RestController
@RequestMapping("/roles")
public class BaseRoleController {
    private final BaseRoleService baseRoleService;
    private final BaseRoleUserService baseRoleUserService;

    @ApiOperation(value = "获取角色列表")
    @GetMapping
    public ApiResult<List<BaseRole>> list(){
        return ApiResult.success(baseRoleService.list());
    }

    @ApiOperation(value = "获取角色分页列表")
    @GetMapping("/page")
    public ApiPageResult<IPage<BaseRole>> page(PageForm pageForm){
        return ApiPageResult.success(baseRoleService.page(new Page(pageForm.getPageNum(), pageForm.getPageCount())));
    }

    @ApiOperation(value = "获取角色下所有用户")
    @GetMapping("/{roleId}/users")
    public ApiResult<List<BaseRoleUser>> getRoleUsers(@PathVariable Long roleId) {
        return ApiResult.<List<BaseRoleUser>>success().data(baseRoleUserService.list(Wrappers.<BaseRoleUser>lambdaQuery().eq(BaseRoleUser::getRoleId, roleId)));
    }

    @ApiOperation(value = "检查编码是否已存在")
    @GetMapping("/checkCode")
    public ApiResult<Boolean> page(@RequestParam String roleCode){
        boolean exist = baseRoleService.count(Wrappers.<BaseRole>lambdaQuery().eq(BaseRole::getRoleCode, roleCode)) > 0;
        return ApiResult.success(exist).message("角色编码已存在.");
    }

    @ApiOperation("查看详情")
    @GetMapping("/{roleId}")
    public ApiResult<BaseRole> detail(@PathVariable Long roleId){
        return ApiResult.success(baseRoleService.getById(roleId));
    }

    @ApiOperation(value = "添加角色", notes = "返回新增id")
    @PostMapping
    public ApiResult<Long> add(@Validated @RequestBody RoleForm roleForm){
        BaseRole baseRole = new BaseRole();
        baseRole.setRoleCode(roleForm.getRoleCode());
        baseRole.setRoleName(roleForm.getRoleName());
        baseRole.setRoleDesc(roleForm.getRoleDesc());
        return ApiResult.<Long>success().data(baseRoleService.addRole(baseRole));
    }

    @ApiOperation("更新角色")
    @PutMapping("/{roleId}")
    public ApiResult update(@PathVariable Long roleId, @Validated @RequestBody RoleForm roleForm){
        BaseRole baseRole = new BaseRole();
        baseRole.setRoleId(roleId);
        baseRole.setRoleCode(roleForm.getRoleCode());
        baseRole.setRoleName(roleForm.getRoleName());
        baseRole.setRoleDesc(roleForm.getRoleDesc());
        baseRoleService.updateRole(baseRole);
        return ApiResult.success();
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/{roleId}")
    public ApiResult del(@PathVariable Long roleId){
        baseRoleService.delRole(roleId);
        return ApiResult.success();
    }

    @ApiOperation(value = "角色添加用户")
    @PostMapping("/{roleId}/users")
    public ApiResult addRoleUsers(@PathVariable Long roleId, @RequestParam List<Long> userIds){
        baseRoleUserService.addRoleUsers(roleId, userIds);
        return ApiResult.success();
    }
}
