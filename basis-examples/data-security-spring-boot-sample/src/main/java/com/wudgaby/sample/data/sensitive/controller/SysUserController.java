package com.wudgaby.sample.data.sensitive.controller;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.sample.data.sensitive.entity.SysRole;
import com.wudgaby.sample.data.sensitive.entity.SysUser;
import com.wudgaby.sample.data.sensitive.entity.SysUserRole;
import com.wudgaby.sample.data.sensitive.service.SysRoleService;
import com.wudgaby.sample.data.sensitive.service.SysUserRoleService;
import com.wudgaby.sample.data.sensitive.service.SysUserService;
import com.wudgaby.sample.data.sensitive.vo.UserForm;
import com.wudgaby.sample.data.sensitive.vo.UserRoleVo;
import com.wudgaby.starter.data.security.sensitive.annotation.ApiSensitive;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/sysUsers")
@RequiredArgsConstructor
public class SysUserController {
    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;
    private final SysUserRoleService sysUserRoleService;

    private static PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @ApiOperation("用户列表")
    @GetMapping
    @ApiSensitive
    public ApiPageResult<SysUser> userPageList(PageForm pageForm){
        return ApiPageResult.success(sysUserService.page(
                new Page<>(pageForm.getPageNum(), pageForm.getPageSize()),
                Wrappers.<SysUser>lambdaQuery().orderByDesc(SysUser::getCreateTime)));
    }

    @ApiOperation("用户列表2")
    @GetMapping("/list2")
    public ApiPageResult<SysUser> userPageList2(PageForm pageForm){
        return ApiPageResult.success(sysUserService.page(
                new Page<>(pageForm.getPageNum(), pageForm.getPageSize()),
                Wrappers.<SysUser>lambdaQuery().orderByDesc(SysUser::getCreateTime)));
    }

    @ApiOperation("添加用户")
    @PostMapping
    public ApiResult addUser(@Validated @RequestBody SysUser sysUser){
        sysUser.setSalt(RandomUtil.randomString(10));
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        sysUserService.save(sysUser);
        return ApiResult.success().data(sysUser);
    }

    @ApiOperation("添加用户2")
    @PostMapping("/userForm")
    public ApiResult addUser(@Validated @RequestBody UserForm userForm){
        userForm.getFrom1().setPassword(passwordEncoder.encode(userForm.getFrom1().getPassword()));
        sysUserService.addUserForm(userForm);
        return ApiResult.success().data(userForm);
    }

    @ApiOperation("添加角色")
    @PostMapping("/role")
    public ApiResult addRole(@Validated @RequestBody SysRole sysRole){
        sysRoleService.save(sysRole);
        return ApiResult.success().data(sysRole);
    }

    @ApiOperation("获取用户角色")
    @GetMapping("/userRole")
    public ApiResult<List<UserRoleVo>> getUserRoles(@RequestParam Long userId){
        return ApiResult.<List<UserRoleVo>>success().data(sysUserService.getUserRoleList(userId));
    }

    @ApiOperation("新增用户角色关系")
    @PostMapping("/userRole")
    public ApiResult addUserRole(@RequestBody SysUserRole sysUserRole){
        sysUserRoleService.save(sysUserRole);
        return ApiResult.success();
    }

    @ApiOperation("删除用户角色关系")
    @DeleteMapping("/userRole")
    public ApiResult delUserRole(@RequestBody Long id){
        sysUserRoleService.removeById(id);
        return ApiResult.success();
    }
}
