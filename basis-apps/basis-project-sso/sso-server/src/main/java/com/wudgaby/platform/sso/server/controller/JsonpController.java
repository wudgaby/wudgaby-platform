package com.wudgaby.platform.sso.server.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import com.wudgaby.platform.sso.server.entity.BaseApp;
import com.wudgaby.platform.sso.server.service.BaseAppService;
import com.wudgaby.platform.utils.JacksonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "jsonp模式")
@Controller
@AllArgsConstructor
public class JsonpController {
    private final BaseAppService baseAppService;

    @ApiOperation("跨域获取cookies")
    @GetMapping(value = "/sso/cookies")
    @ResponseBody
    public String auth(@RequestParam("callback") String callback,
                       @RequestParam(SsoConst.APP_ID) String appId,
                       HttpServletRequest request) {
        SsoUserVo ssoUserVo = (SsoUserVo) request.getSession().getAttribute(SsoConst.SSO_USER);

        if(ssoUserVo == null){
            return StrUtil.format("{}({})", callback, JacksonUtil.serialize(ApiResult.failure().message("未登录.")));
        }

        if(StringUtils.isNotBlank(appId)) {
            BaseApp baseApp = baseAppService.getOne(Wrappers.<BaseApp>lambdaQuery().eq(BaseApp::getAppCode, appId));
            if (baseApp == null) {
                return StrUtil.format("{}({})", callback, JacksonUtil.serialize(ApiResult.failure().message("未知应用.")));
            }
            if (baseApp.getStatus() == 0) {
                return StrUtil.format("{}({})", callback, JacksonUtil.serialize(ApiResult.failure().message("该应用未开启.")));
            }
            return StrUtil.format("{}({})", callback, JacksonUtil.serialize(ApiResult.success().data(request.getSession().getId())));
        }
        return StrUtil.format("{}({})", callback, JacksonUtil.serialize(ApiResult.failure().message("未知应用")));
    }
}
