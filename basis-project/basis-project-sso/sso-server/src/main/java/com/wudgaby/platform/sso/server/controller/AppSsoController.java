package com.wudgaby.platform.sso.server.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.config.SsoProperties;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.helper.SsoTokenHelper;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import com.wudgaby.platform.sso.server.entity.User;
import com.wudgaby.platform.sso.server.form.LoginForm;
import com.wudgaby.platform.sso.server.service.RoleService;
import com.wudgaby.platform.sso.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName : AppSsoController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/1 18:40
 * @Desc :   TODO
 */
@Api(tags = "app单点")
@AllArgsConstructor
@RestController
@RequestMapping("/app")
public class AppSsoController {
    private final RoleService roleService;
    private final UserService userService;
    private final SsoTokenHelper ssoTokenHelper;
    private final SsoProperties ssoProperties;

    @ApiOperation("登录")
    @PostMapping(SsoConst.SSO_LOGIN_URL)
    public ApiResult login(@RequestBody @Validated LoginForm loginForm){
        User user = userService.login(loginForm.getAccount(), loginForm.getPassword());
        List<String> roleList = roleService.getRoleListByUserId(user.getId());

        SsoUserVo ssoUserVo = new SsoUserVo();
        BeanUtil.copyProperties(user, ssoUserVo);
        ssoUserVo.setRoleList(roleList);
        ssoUserVo.setVersion(UUID.fastUUID().toString())
                .setExpireMin(ssoProperties.getSessionTimeout())
                .setExpireFreshTime(System.currentTimeMillis())
        ;

        /*String sessionId = SsoSessionIdHelper.buildSessionId(ssoUserVo);
        ssoUserVo.setToken(sessionId);

        ssoTokenHelper.login(ssoUserVo);

        return ApiResult.success().data(sessionId);*/

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.getSession().setAttribute(SsoConst.SSO_USER, ssoUserVo);
        return ApiResult.success().data(request.getSession().getId());
    }

    @ApiOperation("检查状态")
    @GetMapping(SsoConst.SSO_CHECK_URL)
    public ApiResult check(@RequestParam(required = false) String sessionId){
        /*SsoUserVo ssoUserVo = ssoTokenHelper.loginCheck(sessionId);
        if(ssoUserVo == null){
            return ApiResult.createInstance(SystemResultCode.TOKEN_IS_INVALID);
        }*/

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        SsoUserVo ssoUserVo = (SsoUserVo) request.getSession().getAttribute(SsoConst.SSO_USER);
        return ApiResult.success().data(ssoUserVo);
    }

    @ApiOperation("登出")
    @DeleteMapping(SsoConst.SSO_LOGOUT_URL)
    public ApiResult logout(@RequestParam(required = false) String sessionId, HttpServletRequest request){
        /*if(StringUtils.isNotBlank(sessionId)){
            ssoTokenHelper.logout(sessionId);
        }else{
            ssoTokenHelper.logout(request);
        }*/
        request.getSession().invalidate();
        return ApiResult.success();
    }
}
