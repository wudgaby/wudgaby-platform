package com.wudgaby.platform.socketio.server;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.namespace.Namespace;
import com.corundumstudio.socketio.store.RedissonStoreFactory;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/2/2 0002 17:56
 * @desc :
 */
@Slf4j
@Configuration
public class SocketServerConfig {
    @Value("${socket.port}")
    private int port;
    @Bean
    public SocketIOServer socketIOServer(){
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("localhost");
        config.setPort(port);
        // 设置是否可以跨域访问, 不能设置为*
        //config.setOrigin("http://127.0.0.1:8080");
        config.setAllowHeaders("*");
        //基于连接的auth
        config.setAuthorizationListener(new DefaultAuthorizationListener());

        config.setMaxFramePayloadLength(1024 * 1024);
        config.setMaxHttpContentLength(1024 * 1024);
        config.setContext("/socket.io");

        //socket配置
        config.getSocketConfig().setReuseAddress(true);
        config.getSocketConfig().setSoLinger(0);
        config.getSocketConfig().setTcpNoDelay(true);
        config.getSocketConfig().setTcpKeepAlive(true);

        // 基于redis存储
        config.setStoreFactory(getRedisStoreFactory());

        SocketIOServer socketIOServer =  new SocketIOServer(config);
        socketIOServer.addConnectListener(client -> {
            log.info("addConnectListener");
        });
        socketIOServer.addDisconnectListener(client -> {
            log.info("addDisconnectListener");
        });
        //基于命名空间auth
        socketIOServer.getNamespace(Namespace.DEFAULT_NAME).addAuthTokenListener(new DefaultAuthTokenListener());


        //获取所有命名空间下的所有客户端
        //socketIOServer.getBroadcastOperations().getClients();
        //获取默认命名空间下的所有客户端
        //socketIOServer.getAllClients();
        //获取指定命名空间下的所有客户端
        //socketIOServer.getNamespace("/").getBroadcastOperations();

        return socketIOServer;
    }

    /*@Bean(name = "defaultNS")
    public SocketIONamespace getDefaultNameSpace(MessageEventHandler messageEventHandler) {
        socketIOServer().getNamespace(Namespace.DEFAULT_NAME).addListeners(messageEventHandler);
        return socketIOServer().getNamespace(Namespace.DEFAULT_NAME);
    }*/

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(){
        return new SpringAnnotationScanner(socketIOServer());
    }

    private RedissonStoreFactory getRedisStoreFactory(){
        Config redissonConfig = new Config();
        redissonConfig.useSingleServer().setAddress("redis://localhost:6379");
        redissonConfig.useSingleServer().setDatabase(1);
        //redissonConfig.useSingleServer().setPassword("redis-pwd");
        RedissonClient redisson = Redisson.create(redissonConfig);
        RedissonStoreFactory redisStoreFactory = new RedissonStoreFactory(redisson);
        return redisStoreFactory;
    }

    private void ack(SocketIOServer socketIOServer){
        //ACK
        socketIOServer.addEventListener("ackevent1", SocketMessage.class, new DataListener<SocketMessage>() {
            @Override
            public void onData(final SocketIOClient client, SocketMessage data, final AckRequest ackRequest) {
                // check is ack requested by client,
                // but it's not required check
                if (ackRequest.isAckRequested()) {
                    // send ack response with data to client
                    ackRequest.sendAckData("client message was delivered to server!", "yeah!");
                }

                // send message back to client with ack callback WITH data
                SocketMessage ackChatObjectData = new SocketMessage(data.getUserName(), "message with ack data");
                client.sendEvent("ackevent2", new AckCallback<String>(String.class) {
                    @Override
                    public void onSuccess(String result) {
                        System.out.println("ack from client: " + client.getSessionId() + " data: " + result);
                    }
                }, ackChatObjectData);

                SocketMessage ackChatObjectData1 = new SocketMessage(data.getUserName(), "message with void ack");
                client.sendEvent("ackevent3", new VoidAckCallback() {
                    protected void onSuccess() {
                        System.out.println("void ack from: " + client.getSessionId());
                    }

                }, ackChatObjectData1);
            }
        });
    }

    private void binary(SocketIOServer socketIOServer){
        socketIOServer.addEventListener("msg", byte[].class, new DataListener<byte[]>() {
            @Override
            public void onData(SocketIOClient client, byte[] data, AckRequest ackRequest) {
                client.sendEvent("msg", data);
            }
        });
    }

    private void chat(SocketIOServer socketIOServer){
        socketIOServer.addEventListener("chatevent", SocketMessage.class, new DataListener<SocketMessage>() {
            @Override
            public void onData(SocketIOClient client, SocketMessage data, AckRequest ackRequest) {
                // broadcast messages to all clients
                socketIOServer.getBroadcastOperations().sendEvent("chatevent", data);
                //ackRequest.sendAckData("{status: \"ok\"}");
            }
        });
    }

    private void namespace(SocketIOServer socketIOServer){
        final SocketIONamespace chat1namespace = socketIOServer.addNamespace("/chat1");
        chat1namespace.addEventListener("message", SocketMessage.class, new DataListener<SocketMessage>() {
            @Override
            public void onData(SocketIOClient client, SocketMessage data, AckRequest ackRequest) {
                // broadcast messages to all clients
                chat1namespace.getBroadcastOperations().sendEvent("message", data);
            }
        });

        final SocketIONamespace chat2namespace = socketIOServer.addNamespace("/chat2");
        chat2namespace.addEventListener("message", SocketMessage.class, new DataListener<SocketMessage>() {
            @Override
            public void onData(SocketIOClient client, SocketMessage data, AckRequest ackRequest) {
                // broadcast messages to all clients
                chat2namespace.getBroadcastOperations().sendEvent("message", data);
            }
        });
    }

    private void ssl(com.corundumstudio.socketio.Configuration config){
        config.setKeyStorePassword("test1234");
        InputStream stream = this.getClass().getResourceAsStream("/keystore.jks");
        config.setKeyStore(stream);
    }
}
