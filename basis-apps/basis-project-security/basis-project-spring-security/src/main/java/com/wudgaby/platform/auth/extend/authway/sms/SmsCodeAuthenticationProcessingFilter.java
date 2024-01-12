package com.wudgaby.platform.auth.extend.authway.sms;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/2/3 20:05
 * @Desc :
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SmsCodeAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter{
    public static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";
    public static final String SPRING_SECURITY_FORM_CAPTCHA_KEY = "captcha";

    private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;
    private String captchaParameter = SPRING_SECURITY_FORM_CAPTCHA_KEY;
    private boolean postOnly = true;

    public SmsCodeAuthenticationProcessingFilter() {
        super(new AntPathRequestMatcher("/mobileLogin", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String mobile = obtainMobile(request);
        String captcha = obtainCaptcha(request);

        if (mobile == null) {
            mobile = "";
        }

        if (captcha == null) {
            captcha = "";
        }

        mobile = mobile.trim();
        captcha = captcha.trim();

        SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(mobile, captcha);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
    }

    protected String obtainCaptcha(HttpServletRequest request) {
        return request.getParameter(captchaParameter);
    }

    protected void setDetails(HttpServletRequest request,
                              AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }


}
