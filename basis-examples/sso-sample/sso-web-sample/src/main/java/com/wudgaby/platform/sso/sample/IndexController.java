package com.wudgaby.platform.sso.sample;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.config.SsoProperties;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xuxueli 2017-08-01 21:39:47
 */
@Controller
@AllArgsConstructor
public class IndexController {
    private final SsoProperties ssoProperties;

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        SsoUserVo ssoUserVo = (SsoUserVo) request.getSession().getAttribute(SsoConst.SSO_USER);
        model.addAttribute(SsoConst.SSO_USER, ssoUserVo);
        model.addAttribute("appCode", ssoProperties.getAppCode());
        model.addAttribute("ssoLogoutUrl", ssoProperties.getServer() + SsoConst.SSO_LOGOUT_URL + "/" + StringUtils.trimToEmpty(ssoProperties.getAppCode()));
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:" + ssoProperties.getServer() + "/mh";
    }

    @GetMapping("/json")
    @ResponseBody
    public ApiResult<SsoUserVo> json(HttpServletRequest request) {
        SsoUserVo ssoUserVo = (SsoUserVo) request.getSession().getAttribute(SsoConst.SSO_USER);
        return ApiResult.<SsoUserVo>success().data(ssoUserVo);
    }

}