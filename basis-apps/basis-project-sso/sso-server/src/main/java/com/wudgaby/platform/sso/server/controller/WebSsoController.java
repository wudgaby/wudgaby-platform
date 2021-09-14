package com.wudgaby.platform.sso.server.controller;

import cn.hutool.core.net.url.UrlBuilder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Charsets;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.config.SsoProperties;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import com.wudgaby.platform.sso.server.entity.BaseApp;
import com.wudgaby.platform.sso.server.entity.User;
import com.wudgaby.platform.sso.server.service.BaseAppService;
import com.wudgaby.platform.sso.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final BaseAppService baseAppService;
    private final SsoProperties ssoProperties;
    private final FindByIndexNameSessionRepository findByIndexNameSessionRepository;

    @ApiOperation("门户")
    @GetMapping("/mh")
    public String index(Model model){
        List<BaseApp> baseAppList = baseAppService.list();
        model.addAttribute("appList", baseAppList);
        return "mh";
    }

    @ApiOperation("首页")
    @GetMapping("/")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response){
        SsoUserVo ssoUserVo = (SsoUserVo) request.getSession().getAttribute(SsoConst.SSO_USER);
        if(ssoUserVo == null){
            return "redirect:" + SsoConst.SSO_LOGIN_URL;
        }
        model.addAttribute("ssoUserVo", ssoUserVo);
        return "index";
    }

    @ApiOperation("进入登录页")
    @GetMapping(value = {SsoConst.SSO_LOGIN_URL + "/{" + SsoConst.APP_ID + "}", SsoConst.SSO_LOGIN_URL})
    public String login(HttpServletRequest request, HttpServletResponse response,
                        RedirectAttributes redirectAttributes,
                        Model model,
                        @PathVariable(name = SsoConst.APP_ID, required = false) String appId,
                        @RequestParam(name = SsoConst.REDIRECT_URL, required = false) String redirectUrl){

        appId = StringUtils.defaultIfEmpty(appId, "");
        model.addAttribute(SsoConst.APP_ID, appId);

        //appId不为空时. 先校验appId. 再校验会话.
        //appId为空时,校验会话
        if(StringUtils.isNotBlank(appId)){
            BaseApp baseApp = baseAppService.getOne(Wrappers.<BaseApp>lambdaQuery().eq(BaseApp::getAppCode, appId));
            if(baseApp == null){
                request.setAttribute(SsoConst.ERROR_MESSAGE, "未知应用");
                request.setAttribute(SsoConst.REDIRECT_URL, redirectUrl);
                return "login";
            }
            if(baseApp.getStatus() == 0){
                request.setAttribute(SsoConst.ERROR_MESSAGE, "该应用未开启");
                request.setAttribute(SsoConst.REDIRECT_URL, redirectUrl);
                return "login";
            }

            SsoUserVo ssoUserVo = (SsoUserVo) request.getSession().getAttribute(SsoConst.SSO_USER);
            if(ssoUserVo == null){
                String errorMsg = Optional.ofNullable(redirectAttributes.getFlashAttributes())
                        .map(m -> Objects.toString(m.get(SsoConst.ERROR_MESSAGE), null))
                        .orElse(request.getParameter(SsoConst.ERROR_MESSAGE));
                request.setAttribute(SsoConst.ERROR_MESSAGE, errorMsg);
                request.setAttribute(SsoConst.REDIRECT_URL, redirectUrl);
                return "login";
            }

            request.getSession().setAttribute(SsoConst.SSO_USER, ssoUserVo);
            request.getSession().setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, ssoUserVo.getAccount());
            String callbackUrl = UrlBuilder.of(StringUtils.isBlank(baseApp.getWebsite()) ? SsoConst.DEFAULT_REDIRECT_URL : baseApp.getWebsite(), Charsets.UTF_8)
                    .addQuery(SsoConst.ACCESS_TOKEN, request.getSession().getId())
                    .addQuery(SsoConst.REDIRECT_URL, redirectUrl)
                    .build();
            return "redirect:" + callbackUrl;
        } else {
            SsoUserVo ssoUserVo = (SsoUserVo) request.getSession().getAttribute(SsoConst.SSO_USER);
            if(ssoUserVo == null){
                String errorMsg = Optional.ofNullable(redirectAttributes.getFlashAttributes())
                        .map(m -> Objects.toString(m.get(SsoConst.ERROR_MESSAGE), null))
                        .orElse(request.getParameter(SsoConst.ERROR_MESSAGE));
                request.setAttribute(SsoConst.ERROR_MESSAGE, errorMsg);
                request.setAttribute(SsoConst.REDIRECT_URL, redirectUrl);
                return "login";
            }
        }
        return "redirect:/";
    }

    @ApiOperation("登录")
    @PostMapping(value = {SsoConst.SSO_LOGIN_URL + "/{" + SsoConst.APP_ID + "}", SsoConst.SSO_LOGIN_URL})
    public String login(@RequestParam String account, @RequestParam String password,
                        @PathVariable(name = SsoConst.APP_ID, required = false) String appId,
                        @RequestParam(name = SsoConst.REDIRECT_URL, required = false) String redirectUrl,
                        RedirectAttributes redirectAttributes,
                        HttpServletRequest request, HttpServletResponse response){

        appId = StringUtils.defaultIfEmpty(appId, "");

        User user;
        try{
            user = userService.login(account, password);
        }catch (BusinessException be){
            redirectAttributes.addFlashAttribute(SsoConst.ERROR_MESSAGE, be.getMessage());
            if(StringUtils.isNotBlank(redirectUrl)) {
                redirectAttributes.addAttribute(SsoConst.REDIRECT_URL, redirectUrl);
            }
            return "redirect:" + SsoConst.SSO_LOGIN_URL + "/" + appId;
        }

        SsoUserVo ssoUserVo = new SsoUserVo()
                .setUserId(String.valueOf(user.getId()))
                .setUsername(user.getUserName())
                .setAccount(user.getAccount())
                .setUserTel(user.getPhone())
                .setUserEmail(user.getEmail())
                .setUserPortrait(user.getAvatar())
                ;

        if(StringUtils.isNotBlank(appId)){
            BaseApp baseApp = baseAppService.getOne(Wrappers.<BaseApp>lambdaQuery().eq(BaseApp::getAppCode, appId));
            if(baseApp == null){
                redirectAttributes.addFlashAttribute(SsoConst.ERROR_MESSAGE, "未知应用");
                if(StringUtils.isNotBlank(redirectUrl)) {
                    redirectAttributes.addAttribute(SsoConst.REDIRECT_URL, redirectUrl);
                }
                return "redirect:" + SsoConst.SSO_LOGIN_URL + "/" + appId ;
            }
            if(baseApp.getStatus() == 0){
                redirectAttributes.addFlashAttribute(SsoConst.ERROR_MESSAGE, "该应用未开启");
                if(StringUtils.isNotBlank(redirectUrl)) {
                    redirectAttributes.addAttribute(SsoConst.REDIRECT_URL, redirectUrl);
                }
                return "redirect:" + SsoConst.SSO_LOGIN_URL + "/" + appId;
            }

            request.getSession().setAttribute(SsoConst.SSO_USER, ssoUserVo);
            request.getSession().setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, ssoUserVo.getAccount());
            String callbackUrl = UrlBuilder.of(StringUtils.isBlank(baseApp.getWebsite()) ? SsoConst.DEFAULT_REDIRECT_URL : baseApp.getWebsite(), Charsets.UTF_8)
                    .addQuery(SsoConst.ACCESS_TOKEN, request.getSession().getId())
                    .addQuery(SsoConst.REDIRECT_URL, redirectUrl)
                    .build();
            return "redirect:" + callbackUrl;
        }

        request.getSession().setAttribute(SsoConst.SSO_USER, ssoUserVo);
        request.getSession().setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, ssoUserVo.getAccount());
        if(StringUtils.isBlank(redirectUrl)){
            return "index";
        }
        return "redirect:" + redirectUrl;
    }

    @ApiOperation("单点登出")
    @GetMapping(value = {SsoConst.SSO_LOGOUT_URL + "/{" + SsoConst.APP_ID + "}", SsoConst.SSO_LOGOUT_URL})
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes,
                               @PathVariable(name = SsoConst.APP_ID, required = false) String appId,
                                @RequestParam(name = SsoConst.REDIRECT_URL, required = false) String redirectUrl){
        request.getSession().invalidate();
        //ModelAndView modelAndView = new ModelAndView("login");
        ModelAndView modelAndView = new ModelAndView("redirect:" + SsoConst.SSO_LOGIN_URL + "/" + StringUtils.trimToEmpty(appId));
        List<String> logoutUrlList = baseAppService.list(Wrappers.<BaseApp>lambdaQuery().isNotNull(BaseApp::getWebsite))
                                            .stream().map(BaseApp::getWebsite)
                                            .map(site -> site + SsoConst.SSO_LOGOUT_URL)
                                            .collect(Collectors.toList());
        //modelAndView.addObject("logoutUrlList", logoutUrlList);
        redirectAttributes.addFlashAttribute("logoutUrlList", logoutUrlList);
        redirectAttributes.addFlashAttribute(SsoConst.ERROR_MESSAGE, "登出成功!");
        return modelAndView;
    }

    @ApiOperation("检查状态")
    @GetMapping(SsoConst.SSO_CHECK_URL)
    @ResponseBody
    public ApiResult check(HttpServletRequest request){
        SsoUserVo ssoUserVo = (SsoUserVo) request.getSession().getAttribute(SsoConst.SSO_USER);
        if(ssoUserVo == null) {
            return ApiResult.failure();
        }
        return ApiResult.success().data(ssoUserVo);
    }
}
