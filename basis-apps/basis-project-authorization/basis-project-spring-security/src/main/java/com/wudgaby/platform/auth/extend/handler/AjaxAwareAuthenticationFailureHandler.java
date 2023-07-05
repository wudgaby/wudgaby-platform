package com.wudgaby.platform.auth.extend.handler;

import com.google.common.base.Charsets;
import com.wudgaby.platform.auth.exceptions.AuthMethodNotSupportedException;
import com.wudgaby.platform.auth.exceptions.JwtExpiredTokenException;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.utils.JacksonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 * @author vladimir.stankovic
 *
 * Aug 3, 2016
 */
//@Component
public class AjaxAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {
		
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setCharacterEncoding(Charsets.UTF_8.name());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		ApiResult apiResult;
		if (e instanceof BadCredentialsException) {
			apiResult = ApiResult.<String>failure().message("无效账号或密码");
		} else if (e instanceof JwtExpiredTokenException) {
			apiResult = ApiResult.<String>failure().message("token已过期");
		} else if (e instanceof AuthMethodNotSupportedException) {
			apiResult = ApiResult.<String>failure().message(e.getMessage());
		}else{
			apiResult = ApiResult.<String>failure().message("认证失败");
		}
		String json = JacksonUtil.serialize(apiResult);
		response.getWriter().write(json);
		response.getWriter().flush();
	}
}
