package com.wudgaby.platform.websocket.listener;

import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName : RequestListener
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/27 2:31
 * @Desc :
 * http://www.cnblogs.com/zhuxiaojie/p/6238826.html
 *  配置监听器，将所有request请求都携带上httpSession
 *  用于webSocket取Session
 *
 *  或者也可以使用@WebListener注解
 * 然后使用@ServletComponentScan注解，配置在启动方法上面。
 */
//@WebListener
@Component
public class RequestListener implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        //将所有request请求都携带上httpSession
        ((HttpServletRequest) sre.getServletRequest()).getSession();
    }

    public RequestListener() {
    }

    @Override
    public void requestDestroyed(ServletRequestEvent arg0) {
    }
}