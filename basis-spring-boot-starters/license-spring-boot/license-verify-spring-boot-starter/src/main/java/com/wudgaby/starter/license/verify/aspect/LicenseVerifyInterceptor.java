package com.wudgaby.starter.license.verify.aspect;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.starter.license.verify.LicenseVerify;
import com.wudgaby.starter.license.verify.config.LicenseProp;
import com.wudgaby.starter.license.verify.enums.InstallEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/19 0019 15:46
 * @desc :
 */
@Slf4j
@RequiredArgsConstructor
public class LicenseVerifyInterceptor implements HandlerInterceptor {
    private final LicenseProp licenseProp;
    private final LicenseVerify licenseVerify;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @SuppressWarnings("deprecation")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //静态资源请求
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        //校验是否放行
        HandlerMethod hm = (HandlerMethod) handler;
        if(hm.getBeanType() == BasicErrorController.class) {
            return true;
        }

        if(licenseProp.getInstallType() == InstallEnum.IMPORT){
            boolean isMatch = pathMatcher.match(licenseProp.getImportLicenseUrl(), request.getRequestURI());
            if(isMatch){
                return true;
            }
        }

        boolean isPass = licenseVerify.verifyFromCache();
        if(isPass){
            return true;
        }

        ServletUtil.write(response, JSONUtil.toJsonStr(ApiResult.failure("软件未授权!请联系软件厂商授权后使用.")), MediaType.APPLICATION_JSON_UTF8_VALUE);
        return false;
    }
}
