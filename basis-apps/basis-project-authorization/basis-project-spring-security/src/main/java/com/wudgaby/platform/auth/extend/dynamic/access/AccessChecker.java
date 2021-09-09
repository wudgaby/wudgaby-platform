package com.wudgaby.platform.auth.extend.dynamic.access;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface AccessChecker extends InitializingBean {
    boolean check(Authentication authentication, HttpServletRequest request);

    boolean check(Authentication authentication, String param);
}
