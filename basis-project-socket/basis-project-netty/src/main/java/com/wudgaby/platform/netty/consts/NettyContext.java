package com.wudgaby.platform.netty.consts;

import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName : NettyContext
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/30 14:41
 * @Desc :   TODO
 */
public class NettyContext {
    public static final String WEBSOCKET_PATH = "/websocket/netty";
    public static Map<String, WebSocketServerHandshaker> webSocketHandshakerMap = new ConcurrentHashMap<>();
}
