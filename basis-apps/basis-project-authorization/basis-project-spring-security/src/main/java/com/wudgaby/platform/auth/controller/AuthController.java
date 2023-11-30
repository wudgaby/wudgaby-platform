package com.wudgaby.platform.auth.controller;

import com.wudgaby.platform.auth.extend.authway.email.EmailAuthenticationToken;
import com.wudgaby.platform.auth.extend.authway.username.LoginForm;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.security.core.SecurityUtils;
import com.wudgaby.platform.springext.RequestContextHolderSupport;
import com.wudgaby.redis.api.RedisSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName : AuthController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/3 13:48
 * @Desc :
 */
@Slf4j
@Api(tags = "认证")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private RedisSupport redisSupport;

    @Autowired(required = false)
    private AuthenticationManager authenticationManager;

    @Autowired(required = false)
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @ApiOperation("账号登录")
    @PostMapping("/usernameLogin")
    public ApiResult usernameLogin(@RequestParam String username, @RequestParam String password){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        //Authentication authentication = authenticationManagerBuilder.getObject().authenticate(token);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        RequestContextHolderSupport.getRequest().getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        return ApiResult.success().message("登陆成功").data(authentication.getPrincipal());
    }

    @ApiOperation("邮箱登录")
    @PostMapping("/emailLogin")
    public ApiResult emailLogin(@RequestParam String email, @RequestParam String password,
                                @RequestParam String imageCode, @RequestParam String timestamp){
        EmailAuthenticationToken token = new EmailAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        RequestContextHolderSupport.getRequest().getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        return ApiResult.success().message("登陆成功").data(authentication.getPrincipal());
    }

    @ApiOperation("ajax登录")
    @PostMapping("/ajaxLogin")
    public String ajaxLogin(@RequestBody LoginForm loginForm){
        return "登陆成功";
    }

    @ApiIgnore
    @PostMapping("/failure")
    public String loginFailure(HttpServletRequest request){
        Exception exception = (Exception) request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        return exception.getMessage();
    }

    @ApiIgnore
    @PostMapping("/success")
    public String loginSuccess(){
        return SecurityUtils.getCurrentUser().getUsername() + " 登录成功!";
    }

    @ApiOperation("登出")
    @DeleteMapping("/logout")
    public String logout(){
        SecurityContextHolder.clearContext();
        RequestContextHolderSupport.getRequest().getSession().invalidate();
        return "登出成功";
    }

    @ApiOperation("发送手机验证码")
    @GetMapping("/smscode")
    public String sendSmsCode(@RequestParam String mobile){
        String smsCode = RandomStringUtils.randomNumeric(6);
        redisSupport.set("smscode:" + mobile, smsCode, 300);
        log.info("向手机 {} 发送验证码 {}.", mobile, smsCode);
        return smsCode;
    }

    @ApiOperation("发送验证码")
    @GetMapping("/captcha")
    public String sendCaptcha(@RequestParam String timestamp){
        String captcha = RandomStringUtils.randomNumeric(6);
        redisSupport.set(timestamp, captcha, 300);
        log.info("{} 生成验证码 {}.", timestamp, captcha);
        return captcha;
    }
}
