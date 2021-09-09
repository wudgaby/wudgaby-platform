package com.wudgaby.platform.sso.server.support;

import org.apache.commons.lang3.StringUtils;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName : HeaderCookieHttpSessionIdResolver
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/12/8 14:49
 * @Desc :   同时支持 cookie 和 header 传递. 优先header
 */
public class HeaderCookieHttpSessionIdResolver implements HttpSessionIdResolver {
    private static final String WRITTEN_SESSION_ID_ATTR = CookieHttpSessionIdResolver.class.getName().concat(".WRITTEN_SESSION_ID_ATTR");
    private static final String HEADER_X_AUTH_TOKEN = "X-AUTH-TOKEN";

    private final String headerName;

    private CookieSerializer cookieSerializer = new DefaultCookieSerializer();

    public static HeaderCookieHttpSessionIdResolver xAuthToken() {
        return new HeaderCookieHttpSessionIdResolver(HEADER_X_AUTH_TOKEN);
    }

    /**
     * The name of the header to obtain the session id from.
     * @param headerName the name of the header to obtain the session id from.
     */
    public HeaderCookieHttpSessionIdResolver(String headerName) {
        if (headerName == null) {
            throw new IllegalArgumentException("headerName cannot be null");
        }
        this.headerName = headerName;
    }

    public void setCookieSerializer(CookieSerializer cookieSerializer) {
        if (cookieSerializer == null) {
            throw new IllegalArgumentException("cookieSerializer cannot be null");
        }
        this.cookieSerializer = cookieSerializer;
    }

    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {
        String headerValue = request.getHeader(headerName);
        if (StringUtils.isNotBlank(headerValue)) {
            return Collections.singletonList(headerValue);
        }
        return this.cookieSerializer.readCookieValues(request);
    }

    @Override
    public void setSessionId(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        response.setHeader(headerName, sessionId);
        if (!sessionId.equals(request.getAttribute(WRITTEN_SESSION_ID_ATTR))) {
            request.setAttribute(WRITTEN_SESSION_ID_ATTR, sessionId);
            this.cookieSerializer.writeCookieValue(new CookieSerializer.CookieValue(request, response, sessionId));
        }
    }

    @Override
    public void expireSession(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(headerName, "");
        this.cookieSerializer.writeCookieValue(new CookieSerializer.CookieValue(request, response, ""));
    }
}
