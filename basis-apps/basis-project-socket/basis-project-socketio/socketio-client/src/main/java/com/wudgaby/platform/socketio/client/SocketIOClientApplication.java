package com.wudgaby.platform.socketio.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/3/4 0004 9:49
 * @desc :
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby")
public class SocketIOClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocketIOClientApplication.class, args);
    }
}
