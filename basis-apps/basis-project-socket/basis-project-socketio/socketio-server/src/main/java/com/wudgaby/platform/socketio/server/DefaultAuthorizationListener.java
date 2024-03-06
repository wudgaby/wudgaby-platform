package com.wudgaby.platform.socketio.server;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.AuthorizationResult;
import com.corundumstudio.socketio.HandshakeData;
import lombok.extern.slf4j.Slf4j;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/3/4 0004 18:16
 * @desc :
 */
@Slf4j
public class DefaultAuthorizationListener implements AuthorizationListener {
    @Override
    public AuthorizationResult getAuthorizationResult(HandshakeData data) {
        //不能使用authToken校验
        log.info("DefaultAuthorizationListener : {}", data);
        /*String token = data.getSingleUrlParam("token");
        if(StrUtil.isBlank(token)) {
            return new AuthorizationResult(false);
        }*/
        return new AuthorizationResult(true);
    }
}
