package com.wudgaby.platform.netty.consts;

import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/30 14:41
 * @Desc :
 */
public class NettyContext {
    public static final String WEBSOCKET_PATH = "/websocket/netty";
    public static Map<String, WebSocketServerHandshaker> webSocketHandshakerMap = new ConcurrentHashMap<>();
}
