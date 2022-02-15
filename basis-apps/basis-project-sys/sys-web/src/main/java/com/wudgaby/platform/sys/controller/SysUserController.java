package com.wudgaby.platform.sys.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.sys.entity.*;
import com.wudgaby.platform.sys.form.UpdatePwdForm;
import com.wudgaby.platform.sys.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final SysUserOrgService sysUserOrgService;
    private final SysUserDeptService sysUserDeptService;
    private final SysUserPostService sysUserPostService;
    private final SysUserResService sysUserResService;
    private static PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

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

    @ApiOperation("修改密码")
    @PutMapping("/pwd")
    public ApiResult updatePassword(@Validated @RequestBody UpdatePwdForm updatePwdForm){
        SysUser dbSysUser = sysUserService.getById(updatePwdForm.getId());
        AssertUtil.notNull(dbSysUser, "该用户不存在");

        AssertUtil.isTrue(passwordEncoder.matches(updatePwdForm.getOldPwd(), dbSysUser.getPassword()), "旧密码不正确");

        SysUser sysUser = new SysUser();
        sysUser.setId(updatePwdForm.getId());
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
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

    @ApiOperation("新增用户机构关系")
    @PostMapping("/userOrg")
    public ApiResult addUserOrg(@RequestBody SysUserOrg sysUserOrg){
        sysUserOrgService.save(sysUserOrg);
        return ApiResult.success();
    }

    @ApiOperation("删除用户机构关系")
    @DeleteMapping("/userOrg")
    public ApiResult delUserOrg(@RequestBody Long id){
        sysUserOrgService.removeById(id);
        return ApiResult.success();
    }

    @ApiOperation("新增用户部门关系")
    @PostMapping("/userDept")
    public ApiResult addUserDept(@RequestBody SysUserDept sysUserDept){
        sysUserDeptService.save(sysUserDept);
        return ApiResult.success();
    }

    @ApiOperation("删除用户部门关系")
    @DeleteMapping("/userDept")
    public ApiResult delUserDept(@RequestBody Long id){
        sysUserDeptService.removeById(id);
        return ApiResult.success();
    }

    @ApiOperation("新增用户职位关系")
    @PostMapping("/userPost")
    public ApiResult addUserPost(@RequestBody SysUserPost sysUserPost){
        sysUserPostService.save(sysUserPost);
        return ApiResult.success();
    }

    @ApiOperation("删除用户职位关系")
    @DeleteMapping("/userPost")
    public ApiResult delUserPost(@RequestBody Long id){
        sysUserPostService.removeById(id);
        return ApiResult.success();
    }

    @ApiOperation("新增用户资源关系")
    @PostMapping("/userRes")
    public ApiResult addUserRes(@RequestBody SysUserRes sysUserRes){
        sysUserResService.save(sysUserRes);
        return ApiResult.success();
    }

    @ApiOperation("删除用户资源关系")
    @DeleteMapping("/userRes")
    public ApiResult delUserRes(@RequestBody Long id){
        sysUserResService.removeById(id);
        return ApiResult.success();
    }
}
