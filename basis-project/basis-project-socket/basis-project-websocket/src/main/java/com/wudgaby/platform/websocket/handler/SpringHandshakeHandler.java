package com.wudgaby.platform.websocket.handler;

import com.wudgaby.platform.websocket.consts.SysConsts;
import com.wudgaby.platform.websocket.vo.FastPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

/**
 * @ClassName : SpringHandshakeHandler
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/26 16:19
 * @Desc :   TODO
 */
@Component
public class SpringHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        //将客户端标识封装为Principal对象，从而让服务端能通过getName()方法找到指定客户端
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpServletRequest = servletServerHttpRequest.getServletRequest();
            FastPrincipal fastPrincipal = (FastPrincipal)attributes.get(SysConsts.TOKEN);
            return fastPrincipal;
        }
        return null;
    }
}
