package com.wudgaby.platform.websocket.endpoint;

import com.wudgaby.platform.websocket.coder.MessageDecode;
import com.wudgaby.platform.websocket.coder.MessageEncode;
import com.wudgaby.platform.websocket.config.JavaxWebSocketConfig;
import com.wudgaby.platform.websocket.consts.SysConsts;
import com.wudgaby.platform.websocket.storage.WsSessionManager;
import com.wudgaby.platform.websocket.vo.FastPrincipal;
import com.wudgaby.platform.websocket.vo.WsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * @ClassName : ChatEndpoint
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/25 20:44
 * @Desc :   
 */
@Slf4j
@Component
@ServerEndpoint(value = "/websocket/javax",
        encoders = {MessageEncode.class}, decoders = {MessageDecode.class},
        configurator = JavaxWebSocketConfig.class)
public class JavaxServerEndpoint {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException, EncodeException {
        String name = getCurrentUser(session);
        if(name == null){
            throw new RuntimeException("未登录的用户!");
        }

        WsSessionManager.add(name, session);
        log.info("当前人数: {}", WsSessionManager.getOnlineNum());

        WsMessage msg = new WsMessage();
        msg.setDate(new Date());
        msg.setName(name);
        msg.setMsg("欢迎 " + name + " 进入聊天室.");
        msg.setType(WsMessage.MsgType.NOTICE);
        msg.setOnlineNum(WsSessionManager.getOnlineNum());
        session.getBasicRemote().sendObject(msg);

        msg = new WsMessage();
        msg.setDate(new Date());
        msg.setName(name);
        msg.setMsg(name + " 进入聊天室");
        msg.setType(WsMessage.MsgType.NOTICE);
        msg.setOnlineNum(WsSessionManager.getOnlineNum());

        for(Session s : WsSessionManager.getSessions()){
            if(s == session){
                continue;
            }
            s.getBasicRemote().sendObject(msg);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        String name = getCurrentUser(session);
        //String name = getCurrentUser(config);
        WsSessionManager.removeSession(name);

        WsMessage msg = new WsMessage();
        msg.setDate(new Date());
        msg.setName(name);
        msg.setMsg(name + " 离开了聊天室.");
        msg.setType(WsMessage.MsgType.NOTICE);
        msg.setOnlineNum(WsSessionManager.getOnlineNum());

        for(Session s : WsSessionManager.getSessions()){
            s.getBasicRemote().sendObject(msg);
        }
    }

    @OnMessage
    public void onMessage(Session session, WsMessage wsMessage) throws IOException, EncodeException {
        log.info("收到消息: {}", wsMessage);
        String name = getCurrentUser(session);

        WsMessage msg = new WsMessage();
        msg.setDate(new Date());
        msg.setName(name);
        msg.setMsg(wsMessage.getMsg());
        msg.setType(WsMessage.MsgType.MSG);
        msg.setOnlineNum(WsSessionManager.getOnlineNum());

        for(Session s : WsSessionManager.getSessions()){
            s.getBasicRemote().sendObject(msg);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        log.error(throwable.getMessage(), throwable);
    }

    /**
     * 在构造session时已将endpointConfig中的userProperties全部加入了session中
     * @param session
     * @return
     */
    private String getCurrentUser(Session session){
        HttpSession httpSession= (HttpSession) session.getUserProperties().get(HttpSession.class.getName());
        return Optional.ofNullable(httpSession.getAttribute(SysConsts.SESSION_USER)).map(o -> ((FastPrincipal)o).getName()).orElse("");
    }
}
