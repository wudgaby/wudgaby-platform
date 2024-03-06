package com.wudgaby.platform.socketio.server;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/2/2 0002 16:05
 * @desc :
 */

@Slf4j
@SpringBootApplication(scanBasePackages = "com.wudgaby")
public class SocketIOServerApplication implements CommandLineRunner {
    @Autowired
    private SocketIOServer socketIOServer;

    public static void main(String[] args) {
        SpringApplication.run(SocketIOServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("SocketIO Server启动中...");
        socketIOServer.start();
        log.info("SocketIO Server启动成功");
    }

    @PreDestroy
    public void stop(){
        log.info("SocketIO Server 关闭");
        socketIOServer.stop();
    }
}
