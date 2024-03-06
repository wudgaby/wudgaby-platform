package com.wudgaby.platform.socketio.server;

import com.corundumstudio.socketio.AuthTokenListener;
import com.corundumstudio.socketio.AuthTokenResult;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.extern.slf4j.Slf4j;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/3/6 0006 10:15
 * @desc :
 */
@Slf4j
public class DefaultAuthTokenListener implements AuthTokenListener {
    @Override
    public AuthTokenResult getAuthTokenResult(Object authToken, SocketIOClient client) {
        log.info("DefaultAuthTokenListener authToken: {}", authToken);
        /*if(!"abcd".equals(authToken)){
            return new AuthTokenResult(false, authToken);
        }*/
        return AuthTokenResult.AuthTokenResultSuccess;
    }
}
