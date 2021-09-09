package com.wudgaby.platform.permission.controller;


import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.permission.consts.AuthorityConst;
import com.wudgaby.platform.permission.dto.AuthorityApi;
import com.wudgaby.platform.permission.dto.AuthorityMenu;
import com.wudgaby.platform.permission.dto.AuthorityResource;
import com.wudgaby.platform.permission.dto.OpenAuthority;
import com.wudgaby.platform.permission.entity.BaseAuthorityAction;
import com.wudgaby.platform.permission.entity.BaseUser;
import com.wudgaby.platform.permission.service.BaseAuthorityService;
import com.wudgaby.platform.permission.service.BaseUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统权限-菜单权限、操作权限、API权限 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
@Api(tags = "权限管理")
@RestController
@RequestMapping("/authority")
@AllArgsConstructor
public class BaseAuthorityController {
    private final BaseAuthorityService baseAuthorityService;
    private final BaseUserService baseUserService;

    @ApiOperation(value = "获取所有访问权限列表", notes = "获取所有访问权限列表")
    @GetMapping("/access")
    public ApiResult<List<AuthorityResource>> getAuthorityResource() {
        List<AuthorityResource> result = baseAuthorityService.findAuthorityResource();
        return ApiResult.<List<AuthorityResource>>success().data(result);
    }

    @ApiOperation(value = "获取菜单权限列表", notes = "获取菜单权限列表")
    @GetMapping("/menus")
    public ApiResult<List<AuthorityMenu>> getAuthorityMenu() {
        List<AuthorityMenu> result = baseAuthorityService.findAuthorityMenu(1);
        return ApiResult.<List<AuthorityMenu>>success().data(result);
    }

    @ApiOperation(value = "获取功能权限列表")
    @GetMapping("/actions")
    public ApiResult<List<BaseAuthorityAction>> getAuthorityAction(@RequestParam(value = "actionId") Long actionId) {
        List<BaseAuthorityAction> list = baseAuthorityService.findAuthorityAction(actionId);
        return ApiResult.<List<BaseAuthorityAction>>success().data(list);
    }

    @ApiOperation(value = "获取接口权限列表")
    @GetMapping("/apis")
    public ApiResult<List<AuthorityApi>> getAuthorityApi(@RequestParam(value = "serviceId", required = false) String serviceId) {
        List<AuthorityApi> result = baseAuthorityService.findAuthorityApi(serviceId);
        return ApiResult.<List<AuthorityApi>>success().data(result);
    }

    @ApiOperation(value = "获取角色已分配权限")
    @GetMapping("/roles")
    public ApiResult<List<OpenAuthority>> getAuthorityRole(Long roleId) {
        List<OpenAuthority> result = baseAuthorityService.findAuthorityByRole(roleId);
        return ApiResult.<List<OpenAuthority>>success().data(result);
    }

    @ApiOperation(value = "获取用户已分配权限")
    @GetMapping("/users")
    public ApiResult<List<OpenAuthority>> findAuthorityUser(@RequestParam(value = "userId") Long userId) {
        BaseUser user = baseUserService.getById(userId);
        List<OpenAuthority> result = baseAuthorityService.findAuthorityByUser(userId, AuthorityConst.ADMIN.equals(user.getUserName()));
        return ApiResult.<List<OpenAuthority>>success().data(result);
    }

    @ApiOperation(value = "获取应用已分配接口权限")
    @GetMapping("/apps")
    public ApiResult<List<OpenAuthority>> findAuthorityApp(@RequestParam(value = "appId") String appId) {
        List<OpenAuthority> result = baseAuthorityService.findAuthorityByApp(appId);
        return ApiResult.<List<OpenAuthority>>success().data(result);
    }

    @ApiOperation(value = "分配角色权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间.选填", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", value = "权限ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/role/grant")
    public ApiResult grantAuthorityRole(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "expireTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date expireTime,
            @RequestParam(value = "authorityIds", required = false) List<Long> authorityIds) {
        baseAuthorityService.addAuthorityRole(roleId, expireTime, authorityIds);
        return ApiResult.success();
    }

    @ApiOperation(value = "分配用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间.选填", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", value = "权限ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/user/grant")
    public ApiResult grantAuthorityUser(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "expireTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date expireTime,
            @RequestParam(value = "authorityIds", required = false) List<Long> authorityIds) {
        baseAuthorityService.addAuthorityUser(userId, expireTime, authorityIds);
        return ApiResult.success();
    }

    @ApiOperation(value = "分配应用权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间.选填", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", value = "权限ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/app/grant")
    public ApiResult grantAuthorityApp(
            @RequestParam(value = "appId") String appId,
            @RequestParam(value = "expireTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date expireTime,
            @RequestParam(value = "authorityIds", required = false) List<Long> authorityIds) {
        baseAuthorityService.addAuthorityApp(appId, expireTime, authorityIds);
        return ApiResult.success();
    }

    /**
     * 功能按钮绑定API
     *
     * @param actionId
     * @param authorityIds
     * @return
     */
    @ApiOperation(value = "功能按钮授权")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actionId", required = true, value = "功能按钮ID", paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", required = false, value = "全新ID:多个用,号隔开", paramType = "form"),
    })
    @PostMapping("/action/grant")
    public ApiResult grantAuthorityAction(
            @RequestParam(value = "actionId") Long actionId,
            @RequestParam(value = "authorityIds", required = false) List<Long> authorityIds) {
        baseAuthorityService.addAuthorityAction(actionId, authorityIds);
        return ApiResult.success();
    }
}
