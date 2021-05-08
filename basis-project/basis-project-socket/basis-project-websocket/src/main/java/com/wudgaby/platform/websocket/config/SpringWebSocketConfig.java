package com.wudgaby.platform.websocket.config;

import com.wudgaby.platform.websocket.handler.SpringWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * @ClassName : WebSocketConfig
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/25 19:47
 * @Desc :   TODO
 */
@Configuration
@EnableWebSocket
public class SpringWebSocketConfig implements WebSocketConfigurer{

    @Autowired private HandshakeInterceptor handshakeInterceptor;
    @Autowired private HandshakeHandler handshakeHandler;
    @Autowired private SpringWebSocketHandler springWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(springWebSocketHandler, "/websocket/spring")
                .setHandshakeHandler(handshakeHandler)
                .addInterceptors(handshakeInterceptor)
                .setAllowedOrigins("*")
                //.withSockJS()
                ;

        // 浏览器不支持websocket，使用sockjs建立连接
        registry.addHandler(springWebSocketHandler, "/sockjs/websocket/spring")
                .setHandshakeHandler(handshakeHandler)
                .addInterceptors(handshakeInterceptor)
                .setAllowedOrigins("*")
                .withSockJS()
                ;
    }

}
