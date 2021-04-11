package com.wudgaby.platform.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @ClassName : WebSocketBootstrap
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/25 17:38
 * @Desc :   TODO
 */
//@ServletComponentScan
@SpringBootApplication
public class WebSocketBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(WebSocketBootstrap.class, args);
    }
}
