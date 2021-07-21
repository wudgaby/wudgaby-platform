package com.wudgaby.platform.websocket.storage;

import lombok.experimental.UtilityClass;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName : WsSessionManager
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/26 6:00
 * @Desc :
 */
@UtilityClass
public class WsSessionManager {
    private static AtomicInteger onlineNum = new AtomicInteger();
    private static ConcurrentHashMap<String, WebSocketSession> WEBSOCKET_SESSION_POOL = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Session> SESSION_POOL = new ConcurrentHashMap<>();

    public static void add(String key, WebSocketSession session) {
        incrementOnlineNum();
        WEBSOCKET_SESSION_POOL.put(key, session);
    }

    public static WebSocketSession removeWebSocketSession(String key) {
        decrementOnlineNum();
        return WEBSOCKET_SESSION_POOL.remove(key);
    }

    public static void removeAndCloseWebSocketSession(String key) {
        WebSocketSession session = removeWebSocketSession(key);
        if (session != null) {
            try {
                // 关闭连接
                session.close();
            } catch (IOException e) {
                // todo: 关闭出现异常处理
                e.printStackTrace();
            }
        }
    }

    public static WebSocketSession getWebSocketSession(String key) {
        return WEBSOCKET_SESSION_POOL.get(key);
    }

    public static Collection<WebSocketSession> getWebSocketSessions() {
        return WEBSOCKET_SESSION_POOL.values();
    }

    public static void add(String key, Session session) {
        incrementOnlineNum();
        SESSION_POOL.put(key, session);
    }

    public static Session removeSession(String key) {
        decrementOnlineNum();
        return SESSION_POOL.remove(key);
    }

    public static void removeAndCloseSession(String key) {
        Session session = removeSession(key);
        if (session != null) {
            try {
                // 关闭连接
                session.close();
            } catch (IOException e) {
                // todo: 关闭出现异常处理
                e.printStackTrace();
            }
        }
    }

    public static Session getSession(String key) {
        return SESSION_POOL.get(key);
    }

    public static Collection<Session> getSessions() {
        return SESSION_POOL.values();
    }

    private static int incrementOnlineNum(){
        return onlineNum.incrementAndGet();
    }

    private static int decrementOnlineNum(){
        return onlineNum.decrementAndGet();
    }

    public static int getOnlineNum(){
        return onlineNum.get();
    }
}
