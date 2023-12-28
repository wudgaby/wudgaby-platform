package com.wudgaby.platform.auth.extend.authway;

import com.wudgaby.platform.auth.exceptions.ValidateCodeException;
import com.wudgaby.redis.api.RedisSupport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/2/4 0:48
 * @Desc :
 */
@Component
public class ImageValidateCodeFilter extends OncePerRequestFilter {
    @Autowired
    private AuthenticationFailureHandler authFailureHandler;
    @Autowired
    private RedisSupport redisSupport;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/emailLogin".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {
            try {
                validate(request);
            } catch (ValidateCodeException e) {
                authFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validate(HttpServletRequest request) throws ServletRequestBindingException {
        String codeInRequest = ServletRequestUtils.getStringParameter(request, "imageCode");
        String timestamp = ServletRequestUtils.getStringParameter(request, "timestamp");

        if (StringUtils.isEmpty(codeInRequest)) {
            throw new ValidateCodeException("验证码不能为空");
        }

        String codeInRedis = (String)redisSupport.get(timestamp);
        if (codeInRedis == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (!codeInRequest.equals(codeInRedis)) {
            throw new ValidateCodeException("验证码不匹配");
        }
    }
}
