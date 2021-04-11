package com.wudgaby.oauth2.uaa.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @ClassName : LoginController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/24 19:37
 * @Desc :   TODO
 */
@Slf4j
@Controller
// 必须配置 作用于授权页面
@SessionAttributes("authorizationRequest")
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/mobileLogin")
    public String mobileLogin() {
        return "mobileLogin";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 授权页面
     * @param model
     * @return
     */
    @RequestMapping("/oauth/confirm_access")
    public ModelAndView authorizePage(Map<String, Object> model) {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        // 获取用户名
        String userName = ((UserDetails) SecurityContextHolder.getContext()
                                                            .getAuthentication()
                                                            .getPrincipal())
                                                            .getUsername();
        model.put("userName", userName);
        model.put("clientId", authorizationRequest.getClientId());
        model.put("scopes", authorizationRequest.getScope());
        return new ModelAndView("authorize", model);
    }
}
