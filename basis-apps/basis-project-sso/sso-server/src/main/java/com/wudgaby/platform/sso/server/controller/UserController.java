package com.wudgaby.platform.sso.server.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.security.core.SecurityUtils;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.utils.SsoSecurityUtils;
import com.wudgaby.platform.sso.core.vo.PermissionVo;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import com.wudgaby.platform.sso.server.entity.Resource;
import com.wudgaby.platform.sso.server.entity.User;
import com.wudgaby.platform.sso.server.entity.UserApp;
import com.wudgaby.platform.sso.server.form.ChangePwdForm;
import com.wudgaby.platform.sso.server.service.ResourceService;
import com.wudgaby.platform.sso.server.service.RoleService;
import com.wudgaby.platform.sso.server.service.UserAppService;
import com.wudgaby.platform.sso.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName : UserController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/1 18:40
 * @Desc :
 */
@Api(tags = "用户信息")
@AllArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final ResourceService resourceService;
    private final UserAppService userAppService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation("修改密码")
    @PostMapping("/user/changePwd")
    public ApiResult changePwd(@RequestBody ChangePwdForm changePwdForm){
        String userId = SsoSecurityUtils.getUserInfo().getUserId();
        User user = userService.getById(userId);
        if(user == null) {
            return ApiResult.failure().message("未登录用户");
        }

        if(!user.getPassword().equals(changePwdForm.getOldPwd())){
            return ApiResult.failure().message("密码不正确");
        }

        user.setPassword(changePwdForm.getNewPwd());
        userService.updateById(user);
        return ApiResult.success();
    }

    @ApiOperation("用户列表")
    @GetMapping("/users")
    public ApiResult<List<User>> userList(){
        List<User> userList = userService.list();
        return ApiResult.<List<User>>success().data(userList);
    }

    @ApiOperation("获取用户已分配应用")
    @GetMapping("/{userId}/apps")
    public ApiResult<List<UserApp>> userApps(@PathVariable Long userId){
        List<UserApp> userAppList = userAppService.list(Wrappers.<UserApp>lambdaQuery().eq(UserApp::getUserId, userId));
        return ApiResult.<List<UserApp>>success().data(userAppList);
    }

    @ApiOperation(value = "分配用户应用")
    @PostMapping("/app/grant")
    public ApiResult grantAuthorityApp(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "appIds", required = false) List<Long> appIds) {
        userAppService.remove(Wrappers.<UserApp>lambdaQuery().eq(UserApp::getUserId, userId));
        List<UserApp> userAppList = Lists.newArrayList();
        appIds.forEach(appId -> {
            UserApp userApp = new UserApp();
            userApp.setUserId(userId);
            userApp.setAppId(appId);
            userAppList.add(userApp);
        });
        userAppService.saveBatch(userAppList);
        return ApiResult.success();
    }

    @ApiOperation("用户信息")
    @GetMapping("/user/me")
    @ApiIgnore
    public ApiResult<SsoUserVo> me(@RequestParam(required = false) String sessionId,
                                   @RequestParam(required = false) String userId){
        String uid = userId;
        /*SsoUserVo ssoUserVo = ssoTokenHelper.loginCheck(sessionId);
        if(ssoUserVo != null){
            uid = ssoUserVo.getUserId();
        }

        if(StringUtils.isBlank(uid)){
            throw new BusinessException("未知用户");
        }*/

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        SsoUserVo ssoUserVo = (SsoUserVo) request.getSession().getAttribute(SsoConst.SSO_USER);
        if(ssoUserVo == null){
            throw new BusinessException("未登录");
        }
        uid = ssoUserVo.getUserId();

        User user = userService.getById(uid);
        List<String> roleList = roleService.getRoleListByUserId(NumberUtils.toLong(uid));

        SsoUserVo userInfo = new SsoUserVo();
        BeanUtil.copyProperties(user, userInfo);
        userInfo.setRoleList(roleList);
        return ApiResult.<SsoUserVo>success().data(userInfo);
    }

    @ApiOperation("用户资源")
    @GetMapping("/user/resource")
    @ApiIgnore
    public ApiResult<List<PermissionVo>> me(@RequestParam(required = false) String sessionId,
                                            @RequestParam(required = false) String userId,
                                            @RequestParam String sysCode){
        String uid = userId;
        /*SsoUserVo ssoUserVo = ssoTokenHelper.loginCheck(sessionId);
        if(ssoUserVo != null){
            uid = ssoUserVo.getUserId();
        }

        if(StringUtils.isBlank(uid)){
            throw new BusinessException("未知用户");
        }*/

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        SsoUserVo ssoUserVo = (SsoUserVo) request.getSession().getAttribute(SsoConst.SSO_USER);
        if(ssoUserVo == null){
            throw new BusinessException("未登录");
        }
        uid = ssoUserVo.getUserId();

        List<Resource> resourceList = resourceService.getPermissionList(NumberUtils.toLong(uid), sysCode);
        List<PermissionVo> permissionVoList = resourceList.stream().map(r -> {
            PermissionVo vo = new PermissionVo();
            vo.setCode(r.getPermCode());
            vo.setMethod(r.getResMethod());
            vo.setName(r.getResName());
            vo.setType(r.getResType());
            vo.setUri(r.getResUrl());
            return vo;
        }).collect(Collectors.toList());

        return ApiResult.<List<PermissionVo>>success().data(permissionVoList);
    }
}
