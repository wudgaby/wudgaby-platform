package com.wudgaby.platform.websocket.handler;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.wudgaby.platform.websocket.storage.WsSessionManager;
import com.wudgaby.platform.websocket.vo.WsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

/**
 * @ClassName : SpringSocketHandler
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/26 5:39
 * @Desc :
 */
@Slf4j
@Component
public class SpringWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String name = Optional.ofNullable(session.getPrincipal()).map(Principal::getName).orElse(null);
        if(name == null){
            throw new RuntimeException("未登录的用户!");
        }
        WsSessionManager.add(session.getPrincipal().getName(), session);

        WsMessage msg = new WsMessage();
        msg.setDate(new Date());
        msg.setName(name);
        msg.setMsg("欢迎 " + name + " 进入聊天室.");
        msg.setType(WsMessage.MsgType.NOTICE);
        msg.setOnlineNum(WsSessionManager.getOnlineNum());
        session.sendMessage(new TextMessage(JSONUtil.toJsonStr(msg)));

        msg = new WsMessage();
        msg.setDate(new Date());
        msg.setName(name);
        msg.setMsg(name + " 进入聊天室");
        msg.setType(WsMessage.MsgType.NOTICE);
        msg.setOnlineNum(WsSessionManager.getOnlineNum());

        for(WebSocketSession s : WsSessionManager.getWebSocketSessions()) {
            if (s == session) {
                continue;
            }
            s.sendMessage(new TextMessage(JSONUtil.toJsonStr(msg)));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String name = Optional.ofNullable(session.getPrincipal()).map(Principal::getName).orElse(null);
        String payload = message.getPayload();
        WsMessage receiveMessage = JSONUtil.toBean(payload, WsMessage.class);

        WsMessage msg = new WsMessage();
        msg.setDate(new Date());
        msg.setName(name);
        msg.setMsg(receiveMessage.getMsg());
        msg.setType(WsMessage.MsgType.MSG);
        msg.setOnlineNum(WsSessionManager.getOnlineNum());

        for(WebSocketSession wss : WsSessionManager.getWebSocketSessions()){
            wss.sendMessage(new TextMessage(JSONUtil.toJsonStr(msg)));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String name = Optional.ofNullable(session.getPrincipal()).map(Principal::getName).orElse(null);
        WsSessionManager.removeWebSocketSession(name);

        WsMessage msg = new WsMessage();
        msg.setDate(new Date());
        msg.setName(name);
        msg.setMsg(name + " 离开了聊天室.");
        msg.setType(WsMessage.MsgType.NOTICE);
        msg.setOnlineNum(WsSessionManager.getOnlineNum());

        for(WebSocketSession s : WsSessionManager.getWebSocketSessions()){
            s.sendMessage(new TextMessage(JSONUtil.toJsonStr(msg)));
        }
    }
}
