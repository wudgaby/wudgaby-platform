package com.wudgaby.platform.socketio.server;

import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/2/2 0002 17:59
 * @desc :
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MessageEventHandler {
    private final SocketIOServer socketIOServer;

    @OnConnect
    public void onConnect(SocketIOClient client) {
        String id = client.getHandshakeData().getSingleUrlParam("id");
        NettySocketUtil.addClient(id != null ? id : client.getSessionId().toString(), client);
        log.info("客户端:" + client.getSessionId() + "已连接, id=" + id);

        client.sendEvent("hello", new AckCallback<Object>(Object.class, 5000) {
            @Override
            public void onSuccess(Object result) {
                log.info("hello ack callback: {}", result);
            }
        }, "please acknowledge");
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String id = client.getHandshakeData().getSingleUrlParam("id");
        NettySocketUtil.delClient(id != null ? id : client.getSessionId().toString());
        log.info("客户端:" + client.getSessionId() + "断开连接, id=" + id);
    }

    @OnEvent("messageevent")
    public void onMessage(SocketIOClient client, SocketMessage socketMessage) {
        log.info("发来消息：" + socketMessage.getData());
        //回发消息
        client.sendEvent("messageevent", "我是服务器发送的信息");

        //广播消息
        NettySocketUtil.sendBroadcast("当前时间" + System.currentTimeMillis());
    }

    @OnEvent("chatevent")
    public void chatevent(SocketIOClient client, SocketMessage socketMessage) {
        log.info("发来消息：" + socketMessage.getData());
        NettySocketUtil.sendBroadcast("chatevent", socketMessage);
    }

    @OnEvent("update item")
    public void updateItem(SocketIOClient client, Object message, AckRequest ackRequest) {
        log.info("发来消息：" + message);
        ackRequest.sendAckData("{status: \"ok\"}");
    }

    @OnEvent("timeoutTest")
    public void timeoutTest(SocketIOClient client, Object message, AckRequest ackRequest) {
        log.info("发来消息：" + message);
        ackRequest.sendAckData("{status: \"ok\"}");
    }
}
