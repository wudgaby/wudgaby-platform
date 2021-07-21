package com.wudgaby.platform.sso.core.helper;

import cn.hutool.extra.servlet.ServletUtil;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.store.RedisSessionStorage;
import com.wudgaby.platform.sso.core.vo.SessionIdVo;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName : SsoWebHelper
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/4 10:06
 * @Desc :
 */
@Component
public class SsoWebHelper {
    @Autowired
    private RedisSessionStorage redisSessionStorage;
    @Autowired
    private SsoTokenHelper ssoTokenHelper;
    @Autowired
    private SsoRemoteHelper ssoRemoteHelper;

    public SsoUserVo loginCheck(HttpServletRequest request, HttpServletResponse response, boolean isServer){
        //from cookie
        Cookie cookie = ServletUtil.getCookie(request, SsoConst.SSO_SESSION_ID);
        SsoUserVo ssoUserVo;
        if(cookie != null){
            if(isServer){
                ssoUserVo = ssoTokenHelper.loginCheck(cookie.getValue());
            }else{
                ssoUserVo = ssoRemoteHelper.remoteCheck(cookie.getValue());
            }

            if(ssoUserVo != null){
                return ssoUserVo;
            }
        }

        //remove old cookie
        ServletUtil.addCookie(response, SsoConst.SSO_SESSION_ID, "", 0, SsoConst.COOKIE_PATH, null);

        //from param
        String paramSessionId = request.getParameter(SsoConst.SSO_SESSION_ID);

        if(isServer){
            ssoUserVo = ssoTokenHelper.loginCheck(paramSessionId);
        }else{
            ssoUserVo = ssoRemoteHelper.remoteCheck(paramSessionId);
        }

        if (ssoUserVo != null) {
            ServletUtil.addCookie(response, SsoConst.SSO_SESSION_ID, paramSessionId, -1, SsoConst.COOKIE_PATH, null);
            return ssoUserVo;
        }
        return null;
    }

    public void login(HttpServletResponse response, SsoUserVo ssoUserVo){
        String sessionId = ssoUserVo.getToken();

        ServletUtil.addCookie(response, SsoConst.SSO_SESSION_ID, sessionId, -1, SsoConst.COOKIE_PATH, null);

        SessionIdVo sessionIdVo = SsoSessionIdHelper.parse(sessionId);
        if(sessionIdVo == null){
            throw new RuntimeException("无效sessionId. sessionId:" + sessionId);
        }
        redisSessionStorage.put(sessionIdVo.getStoreKey(), ssoUserVo);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response){
        Cookie cookie = ServletUtil.getCookie(request, SsoConst.SSO_SESSION_ID);
        if(cookie != null){
            String sessionId = cookie.getValue();
            ServletUtil.addCookie(response, SsoConst.SSO_SESSION_ID, "", 0, SsoConst.COOKIE_PATH, null);

            SessionIdVo sessionIdVo = SsoSessionIdHelper.parse(sessionId);
            if(sessionIdVo != null){
                redisSessionStorage.remove(sessionIdVo.getStoreKey());
            }
        }
    }

    public void removeSessionIdByCookie(HttpServletRequest request, HttpServletResponse response) {
        ServletUtil.addCookie(response, SsoConst.SSO_SESSION_ID, "", 0, SsoConst.COOKIE_PATH, null);
    }

    public String getSessionIdByCookie(HttpServletRequest request) {
        Cookie cookie = ServletUtil.getCookie(request, SsoConst.SSO_SESSION_ID);
        return cookie == null ? "" : cookie.getValue();
    }
}
