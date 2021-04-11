package com.wudgaby.oauth2.uaa.enhance.filter;

import com.wudgaby.oauth2.uaa.exception.CaptchaException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName : CaptchaFilter
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/10/9 16:24
 * @Desc :   图片验证码过滤器
 *  OncePerRequestFilter 过滤器只会调用一次
 */
public class CaptchaFilter extends OncePerRequestFilter {

    private AuthenticationFailureHandler failureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //表单登录的post请求
        if (StringUtils.equals("/uaa/login", request.getRequestURI())
                && StringUtils.equalsIgnoreCase("post", request.getMethod())) {
            try {
                validate(request);
            } catch (CaptchaException captchaException) {
                //失败调用我们的自定义失败处理器
                failureHandler.onAuthenticationFailure(request, response, captchaException);
                //后续流程终止
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 图片验证码校验
     *
     * @param request
     */
    private void validate(HttpServletRequest request) throws ServletRequestBindingException {
        // 拿到之前存储的imageCode信息
        String captchaInSession = (String)request.getSession().getAttribute("captcha_session_key_" + ServletRequestUtils.getStringParameter(request, "code"));
        String codeInRequest = ServletRequestUtils.getStringParameter(request, "imageCode");

        if (StringUtils.isBlank(codeInRequest)) {
            throw new CaptchaException("验证码的值不能为空");
        }
        if (captchaInSession == null) {
            throw new CaptchaException("验证码不存在");
        }
        if (!StringUtils.equals(captchaInSession, codeInRequest)) {
            throw new CaptchaException("验证码不匹配");
        }
        //验证通过 移除缓存
        request.getSession().removeAttribute("captcha_session_key_" + ServletRequestUtils.getStringParameter(request, "code"));
    }
}
