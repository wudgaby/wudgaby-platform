package com.wudgaby.platform.sso.server.controller;

import cn.hutool.core.bean.BeanUtil;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.helper.SsoTokenHelper;
import com.wudgaby.platform.sso.core.vo.PermissionVo;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import com.wudgaby.platform.sso.server.entity.Resource;
import com.wudgaby.platform.sso.server.entity.User;
import com.wudgaby.platform.sso.server.service.ResourceService;
import com.wudgaby.platform.sso.server.service.RoleService;
import com.wudgaby.platform.sso.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
 * @Desc :   TODO
 */
@Api(tags = "用户信息")
@AllArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final ResourceService resourceService;
    private final SsoTokenHelper ssoTokenHelper;

    @ApiOperation("用户信息")
    @GetMapping("/sso/user/me")
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
    @GetMapping("/sso/user/resource")
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
