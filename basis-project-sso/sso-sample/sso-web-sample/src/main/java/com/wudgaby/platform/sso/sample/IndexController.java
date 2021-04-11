package com.wudgaby.platform.sso.sample;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.BitSet;

/**
 * @author xuxueli 2017-08-01 21:39:47
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        SsoUserVo ssoUserVo = (SsoUserVo) request.getAttribute(SsoConst.SSO_USER);
        model.addAttribute("ssoUserVo", ssoUserVo);
        return "index";
    }

    @GetMapping("/json")
    @ResponseBody
    public ApiResult<SsoUserVo> json(HttpServletRequest request) {
        SsoUserVo ssoUserVo = (SsoUserVo) request.getAttribute(SsoConst.SSO_USER);
        return ApiResult.<SsoUserVo>success().data(ssoUserVo);
    }

}