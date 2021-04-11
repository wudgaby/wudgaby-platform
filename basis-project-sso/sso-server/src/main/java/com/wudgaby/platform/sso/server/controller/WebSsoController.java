package com.wudgaby.platform.sso.server.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.net.url.UrlBuilder;
import com.google.common.base.Charsets;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.sso.core.config.SsoProperties;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.helper.SsoSessionIdHelper;
import com.wudgaby.platform.sso.core.helper.SsoWebHelper;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import com.wudgaby.platform.sso.server.entity.User;
import com.wudgaby.platform.sso.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName : AppSsoController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/1 18:40
 * @Desc :   TODO
 */
@Api(tags = "web单点")
@AllArgsConstructor
@Controller
public class WebSsoController {
    private final UserService userService;
    private final SsoWebHelper ssoWebHelper;
    private final SsoProperties ssoProperties;

    @ApiOperation("首页")
    @GetMapping("/")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response){
        //SsoUserVo ssoUserVo = ssoWebHelper.loginCheck(request, response, true);
        SsoUserVo ssoUserVo = (SsoUserVo) request.getSession().getAttribute(SsoConst.SSO_USER);
        if(ssoUserVo == null){
            return "redirect:" + SsoConst.SSO_LOGIN_URL;
        }

        model.addAttribute("ssoUserVo", ssoUserVo);
        return "index";
    }

    @ApiOperation("进入登录页")
    @GetMapping(SsoConst.SSO_LOGIN_URL)
    public String login(Model model, HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(name = SsoConst.REDIRECT_URL, required = false) String redirectUrl){
        //SsoUserVo ssoUserVo = ssoWebHelper.loginCheck(request, response, true);
        SsoUserVo ssoUserVo = (SsoUserVo) request.getSession().getAttribute(SsoConst.SSO_USER);
        if(ssoUserVo == null){
            model.addAttribute(SsoConst.ERROR_MESSAGE, request.getParameter(SsoConst.ERROR_MESSAGE));
            model.addAttribute(SsoConst.REDIRECT_URL, redirectUrl);
            return "login";
        }

        if(StringUtils.isNotBlank(redirectUrl)){
            //String sessionId = ssoWebHelper.getSessionIdByCookie(request);
            String callbackUrl = UrlBuilder.of(redirectUrl, Charsets.UTF_8)
                    .addQuery(SsoConst.SSO_SESSION_ID, request.getSession().getId())
                    .build();
            return "redirect:" + callbackUrl;
        }
        return "redirect:/";
    }

    @ApiOperation("登录")
    @PostMapping(SsoConst.SSO_LOGIN_URL)
    public String login(@RequestParam String account, @RequestParam String password,
                        @RequestParam(name = SsoConst.REDIRECT_URL, required = false) String redirectUrl,
                        RedirectAttributes redirectAttributes,
                        HttpServletRequest request,
                        HttpServletResponse response){

        //String redirectUrl = request.getParameter(SsoConst.REDIRECT_URL);
        User user;
        try{
            user = userService.login(account, password);
        }catch (BusinessException be){
            request.setAttribute(SsoConst.ERROR_MESSAGE, be.getMessage());
            redirectAttributes.addAttribute(SsoConst.REDIRECT_URL, redirectUrl);
            return "redirect:" + SsoConst.SSO_LOGIN_URL;
        }

        SsoUserVo ssoUserVo = new SsoUserVo()
                .setUserId(String.valueOf(user.getId()))
                .setVersion(UUID.fastUUID().toString(true))
                .setUsername(user.getUserName())
                .setExpireMin(ssoProperties.getSessionTimeout())
                .setExpireFreshTime(System.currentTimeMillis());

        String sessionId = SsoSessionIdHelper.buildSessionId(ssoUserVo);
        ssoUserVo.setToken(sessionId);

        //ssoWebHelper.login(response, ssoUserVo);
        request.getSession().setAttribute(SsoConst.SSO_USER, ssoUserVo);

        if(StringUtils.isNotBlank(redirectUrl)){
            String callbackUrl = UrlBuilder.of(redirectUrl, Charsets.UTF_8)
                    .addQuery(SsoConst.SSO_SESSION_ID, request.getSession().getId())
                    .build();
            return "redirect:" + callbackUrl;
        }
        return "redirect:/";
    }

    @ApiOperation("登出")
    @GetMapping(SsoConst.SSO_LOGOUT_URL)
    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes,
                         @RequestParam(name = SsoConst.REDIRECT_URL, required = false) String redirectUrl){
        //ssoWebHelper.logout(request, response);
        request.getSession().invalidate();
        redirectAttributes.addAttribute(SsoConst.REDIRECT_URL, redirectUrl);
        return "redirect:" + SsoConst.SSO_LOGIN_URL;
    }
}
