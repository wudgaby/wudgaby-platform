package com.wudgaby.sample.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.security.core.UserInfo;
import com.wudgaby.platform.springext.RequestContextHolderSupport;
import com.wudgaby.sample.domain.SysTenant;
import com.wudgaby.sample.domain.SysUser;
import com.wudgaby.sample.domain.SysUserRole;
import com.wudgaby.sample.service.SysTenantService;
import com.wudgaby.sample.service.SysUserRoleService;
import com.wudgaby.sample.service.SysUserService;
import com.wudgaby.starter.tenant.context.TenantContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private final SysUserRoleService sysUserRoleService;

    private final SysTenantService sysTenantService;

    private static PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @GetMapping("/login")
    @ApiOperation("登录")
    public ApiResult login(String username, String tenantName){
        SysTenant sysTenant = sysTenantService.getOne(Wrappers.<SysTenant>lambdaQuery().eq(SysTenant::getName, tenantName));
        Assert.notNull(sysTenant, "租户不存在");

        TenantContextHolder.setTenantId(sysTenant.getId());
        SysUser sysUser = sysUserService.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUserName, username));
        Assert.notNull(sysUser, "用户不存在");

        UserInfo userInfo = new UserInfo();
        userInfo.setId(sysUser.getId());
        userInfo.setAccount(sysUser.getAccount());
        userInfo.setUsername(sysUser.getUserName());
        userInfo.setPhone(sysUser.getPhone());
        userInfo.setEmail(sysUser.getEmail());
        userInfo.setSex(sysUser.getSex());
        userInfo.setAvatar(sysUser.getAvatar());
        userInfo.setStatus(sysUser.getStatus());
        userInfo.setTenantId(sysUser.getTenantId());

        RequestContextHolderSupport.getRequest().getSession().setAttribute("user", userInfo);
        return ApiResult.success();
    }

    @ApiOperation("用户列表")
    @GetMapping
    public ApiPageResult<SysUser> userPageList(PageForm pageForm){
        return ApiPageResult.success(sysUserService.page(
                new Page<>(pageForm.getPageNum(), pageForm.getPageSize()),
                Wrappers.<SysUser>lambdaQuery().orderByDesc(SysUser::getCreateTime)));
    }

    @ApiOperation("添加用户")
    @PostMapping
    public ApiResult addUser(@Validated @RequestBody SysUser sysUser){
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        sysUserService.save(sysUser);
        return ApiResult.success();
    }

    @ApiOperation("更新用户")
    @PutMapping
    public ApiResult updateUser(@Validated @RequestBody SysUser sysUser){
        sysUser.setPassword(null);
        sysUserService.updateById(sysUser);
        return ApiResult.success();
    }

    @ApiOperation("删除用户")
    @DeleteMapping
    public ApiResult delUser(@RequestParam Long id){
        sysUserService.removeById(id);
        return ApiResult.success();
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
