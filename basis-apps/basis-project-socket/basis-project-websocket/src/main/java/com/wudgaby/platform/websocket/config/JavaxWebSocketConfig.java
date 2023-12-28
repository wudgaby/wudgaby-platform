package com.wudgaby.platform.websocket.config;

import com.wudgaby.platform.websocket.listener.RequestListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/27 1:37
 * @Desc :   http://www.cnblogs.com/zhuxiaojie/p/6238826.html
 * 由于websocket的协议与Http协议是不同的，
 * 所以造成了无法直接拿到session。
 * 但是问题总是要解决的，不然这个websocket协议所用的场景也就没了
 * 重写modifyHandshake，HandshakeRequest request可以获取httpSession
 */
@Slf4j
@Configuration
public class JavaxWebSocketConfig extends ServerEndpointConfig.Configurator implements ApplicationContextAware {
    /**
     * ServerEndpointExporter 作用
     * 这个Bean会自动注册使用@ServerEndpoint注解声明的websocket endpoint
     * @return
     */

    private static volatile BeanFactory context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JavaxWebSocketConfig.context = applicationContext;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return context.getBean(clazz);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


    @Autowired
    private RequestListener requestListener;

    @Bean
    public ServletListenerRegistrationBean<RequestListener> servletListenerRegistrationBean() {
        ServletListenerRegistrationBean<RequestListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();
        servletListenerRegistrationBean.setListener(requestListener);
        return servletListenerRegistrationBean;
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        if(httpSession != null){
            sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
        }else{
            log.error("httpSession为空, 请登录!");
            throw new RuntimeException("请登录!");
        }
    }
}
