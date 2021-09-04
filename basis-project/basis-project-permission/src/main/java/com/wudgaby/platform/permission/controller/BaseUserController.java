package com.wudgaby.platform.permission.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.permission.entity.BaseRole;
import com.wudgaby.platform.permission.entity.BaseRoleUser;
import com.wudgaby.platform.permission.entity.BaseUser;
import com.wudgaby.platform.permission.service.BaseRoleService;
import com.wudgaby.platform.permission.service.BaseRoleUserService;
import com.wudgaby.platform.permission.service.BaseUserService;
import com.wudgaby.platform.permission.vo.UserForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户-管理员信息 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-17
 */
@Api(tags = "用户管理")
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class BaseUserController {
    private final BaseUserService baseUserService;
    private final BaseRoleService baseRoleService;
    private final BaseRoleUserService baseRoleUserService;

    @ApiOperation(value = "获取用户列表")
    @GetMapping
    public ApiResult<List<BaseUser>> list(){
        return ApiResult.success(baseUserService.list());
    }

    @ApiOperation(value = "获取用户分页列表")
    @GetMapping("/page")
    public ApiPageResult<IPage<BaseUser>> page(PageForm pageForm){
        return ApiPageResult.success(baseUserService.page(new Page(pageForm.getPageNum(), pageForm.getPageCount())));
    }

    @ApiOperation(value = "获取用户已分配角色")
    @GetMapping("/{userId}/roles")
    public ApiResult<List<BaseRole>> getUserRoles(@PathVariable Long userId) {
        List<BaseRoleUser> baseRoleUserList = baseRoleUserService.list(Wrappers.<BaseRoleUser>lambdaQuery().eq(BaseRoleUser::getUserId, userId));
        List<BaseRole> baseRoleList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(baseRoleUserList)){
            baseRoleList = baseRoleService.list(Wrappers.<BaseRole>lambdaQuery()
                    .in(BaseRole::getRoleId, baseRoleUserList.stream().map(bru -> bru.getRoleId()).collect(Collectors.toList())));
        }
        return ApiResult.<List<BaseRole>>success().data(baseRoleList);
    }

    @ApiOperation(value = "检查用户名是否已存在")
    @GetMapping("/checkUserName")
    public ApiResult<Boolean> page(@RequestParam String userName){
        boolean exist = baseUserService.count(Wrappers.<BaseUser>lambdaQuery().eq(BaseUser::getUserName, userName)) > 0;
        return exist ? ApiResult.success(true).message("该用户名已存在") : ApiResult.success(false);
    }

    @ApiOperation("查看用户详情")
    @GetMapping("/{userId}")
    public ApiResult<BaseUser> detail(@PathVariable Long userId){
        return ApiResult.success(baseUserService.getById(userId));
    }

    @ApiOperation(value = "添加用户", notes = "返回新增id")
    @PostMapping
    public ApiResult<Long> add(@Validated @RequestBody UserForm userForm){
        BaseUser baseUser = new BaseUser();
        baseUser.setUserName(userForm.getUserName());
        baseUser.setNickName(userForm.getNickName());
        baseUser.setPassword(userForm.getPassword());
        baseUser.setAvatar(userForm.getAvatar());
        baseUser.setEmail(userForm.getEmail());
        baseUser.setMobile(userForm.getMobile());
        baseUser.setUserType(userForm.getUserType());
        baseUser.setCompanyId(userForm.getCompanyId());
        baseUser.setUserDesc(userForm.getUserDesc());
        return ApiResult.<Long>success().data(baseUserService.addUser(baseUser));
    }

    @ApiOperation("更新用户")
    @PutMapping("/{userId}")
    public ApiResult update(@PathVariable Long userId, @Validated @RequestBody UserForm userForm){
        BaseUser baseUser = new BaseUser();
        baseUser.setUserId(userId);
        baseUser.setUserName(userForm.getUserName());
        baseUser.setNickName(userForm.getNickName());
        baseUser.setPassword(userForm.getPassword());
        baseUser.setAvatar(userForm.getAvatar());
        baseUser.setEmail(userForm.getEmail());
        baseUser.setMobile(userForm.getMobile());
        baseUser.setUserType(userForm.getUserType());
        baseUser.setCompanyId(userForm.getCompanyId());
        baseUser.setUserDesc(userForm.getUserDesc());
        baseUserService.updateUser(baseUser);
        return ApiResult.success();
    }

    @ApiOperation(value = "用户分配角色")
    @PostMapping("/{userId}/roles")
    public ApiResult addRoleUsers(@PathVariable Long userId, @RequestParam List<Long> roleIds){
        baseRoleUserService.addUserRoles(userId, roleIds);
        return ApiResult.success();
    }
}
