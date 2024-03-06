package com.wudgaby.platform.socketio.server;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.experimental.UtilityClass;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/3/4 0004 18:19
 * @desc :
 */
@UtilityClass
public class NettySocketUtil {
    private static ConcurrentMap<String, SocketIOClient> socketIOClientMap = new ConcurrentHashMap<>();

    public static void addClient(String key, SocketIOClient client) {
        socketIOClientMap.put(key, client);
    }

    public static void delClient(String key) {
        socketIOClientMap.remove(key);
    }

    public static void send(String key, Object message) {
        SocketIOClient client = socketIOClientMap.get(key);
        if(client != null){
            client.sendEvent("message", message);
        }
    }

    public static void sendEvent(String key, String event, Object message) {
        SocketIOClient client = socketIOClientMap.get(key);
        if(client != null){
            client.sendEvent(event, message);
        }
    }

    public static void sendBroadcast(String event, Object message) {
        for (SocketIOClient client : socketIOClientMap.values()) {
            if (client.isChannelOpen()) {
                client.sendEvent(event, message);
            }
        }
    }

    public static void sendBroadcast(Object message) {
        for (SocketIOClient client : socketIOClientMap.values()) {
            if (client.isChannelOpen()) {
                client.sendEvent("Broadcast", message);
            }
        }
    }
}
