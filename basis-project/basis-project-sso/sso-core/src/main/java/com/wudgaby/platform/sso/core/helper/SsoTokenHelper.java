package com.wudgaby.platform.sso.core.helper;

import cn.hutool.core.util.NumberUtil;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.store.RedisSessionStorage;
import com.wudgaby.platform.sso.core.vo.SessionIdVo;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName : SsoWebHelper
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/4 10:06
 * @Desc :   TODO
 */
@Component
public class SsoTokenHelper {
    @Autowired
    private RedisSessionStorage redisSessionStorage;

    public void login(SsoUserVo ssoUserVo) {
        SessionIdVo sessionIdVo = SsoSessionIdHelper.parse(ssoUserVo.getToken());
        if (sessionIdVo == null) {
            throw new RuntimeException("无效sessionId, sessionId:" + ssoUserVo.getToken());
        }
        redisSessionStorage.put(sessionIdVo.getStoreKey(), ssoUserVo);
    }

    public void logout(String sessionId) {
        SessionIdVo sessionIdVo = SsoSessionIdHelper.parse(sessionId);
        if (sessionIdVo == null) {
            return;
        }
        redisSessionStorage.remove(sessionIdVo.getStoreKey());
    }

    public void logout(HttpServletRequest request) {
        String headerSessionId = request.getHeader(SsoConst.SSO_HEADER_X_TOKEN);
        logout(headerSessionId);
    }

    public SsoUserVo loginCheck(String sessionId){
        SessionIdVo sessionIdVo = SsoSessionIdHelper.parse(sessionId);
        if (sessionIdVo == null) {
            return null;
        }

        SsoUserVo ssoUserVo = redisSessionStorage.get(sessionIdVo.getStoreKey());
        if (ssoUserVo != null) {
            if (ssoUserVo.getVersion().equals(sessionIdVo.getVersion())) {
                // After the expiration time has passed half, Auto refresh
                long expire = (long)NumberUtil.div(TimeUnit.MINUTES.toMillis(ssoUserVo.getExpireMin()), 2, 0);
                if ((System.currentTimeMillis() - ssoUserVo.getExpireFreshTime()) > expire) {
                    ssoUserVo.setExpireFreshTime(System.currentTimeMillis());
                    redisSessionStorage.put(sessionIdVo.getStoreKey(), ssoUserVo);
                }
                return ssoUserVo;
            }
        }
        return null;
    }

    /**
     * 从头部获取sessionId
     * @param request
     * @return
     */
    public SsoUserVo loginCheck(HttpServletRequest request){
        String headerSessionId = request.getHeader(SsoConst.SSO_HEADER_X_TOKEN);
        return loginCheck(headerSessionId);
    }
}
