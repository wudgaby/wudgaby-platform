package com.wudgaby.platform.auth.controller;


import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.security.core.SecurityUtils;
import com.wudgaby.platform.security.core.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-04-17
 */
@Api(tags = "用户信息")
@RestController
@RequestMapping("/user")
public class UserController {
    @ApiOperation("我的信息")
    @GetMapping("/me")
    public ApiResult<UserInfo> me(@CurrentSecurityContext(expression = "authentication") Authentication authentication){
        System.out.println(authentication.getName());
        return ApiResult.<UserInfo>success().data(SecurityUtils.getCurrentUser().orElseThrow(() -> new CredentialsExpiredException("未登录")));
    }
}
