package com.wudgaby.starter.license.verify.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.wudgaby.platform.core.config.ExcludeRegistry;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.starter.license.verify.LicenseVerify;
import com.wudgaby.starter.license.verify.config.LicenseProp;
import com.wudgaby.starter.license.verify.enums.InstallEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/19 0019 15:43
 * @desc :
 */
@Slf4j
@RequiredArgsConstructor
public class LicenseVerifyFilter extends OncePerRequestFilter {
    private final LicenseProp licenseProp;
    private final LicenseVerify licenseVerify;

    @Override
    @SuppressWarnings("deprecation")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, javax.servlet.FilterChain filterChain) throws ServletException, IOException {
        if (licenseProp.getInstallType() == InstallEnum.IMPORT) {
            if (isIgnoreUrl(request)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        boolean isPass = licenseVerify.verifyFromCache();
        if (isPass) {
            filterChain.doFilter(request, response);
            return;
        }

        ServletUtil.write(response, JSONUtil.toJsonStr(ApiResult.failure("软件未授权!请联系软件厂商授权后使用.")), MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    private boolean isIgnoreUrl(HttpServletRequest request) {
        Set<String> ignoreUrls = ExcludeRegistry.ofStaticResource().getAllExcludePatterns();
        ignoreUrls.add(licenseProp.getImportLicenseUrl());

        // 快速匹配，保证性能
        if (CollUtil.contains(ignoreUrls, request.getRequestURI())) {
            return true;
        }
        return false;
    }

}
