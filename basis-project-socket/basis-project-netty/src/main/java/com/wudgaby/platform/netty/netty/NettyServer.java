package com.wudgaby.platform.netty.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * @ClassName : NettyServer
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/28 9:47
 * @Desc :   TODO
 */
@Slf4j
@Component
public class NettyServer {
    private static final String hostname = "127.0.0.1";
    private static final int port = 15101;
    //主线程组
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    //工作线程组
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;

    @PostConstruct
    public void star(){
        ChannelFuture future = null;
        //ServerBootstrap负责初始化netty服务器，并且开始监听端口的socket请求
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(hostname, port))
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ServerChannelInitializer())
                //设置队列大小
                .option(ChannelOption.SO_BACKLOG, 1024)
                //开启心跳包活机制，就是客户端、服务端建立连接处于ESTABLISHED状态，超过2小时没有交流，机制会被启动
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // <2.2.6> 允许较小的数据包的发送，降低延迟
                .childOption(ChannelOption.TCP_NODELAY, true)
                ;

        try {
            log.info("Netty Server 已经启动. 监听端口: {} ", port);
            future = serverBootstrap.bind().sync();
            channel = future.channel();
            //channel.closeFuture().sync();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (future != null && future.isSuccess()) {
                log.info("Netty server listening " + hostname + " on port " + port + " and ready for connections...");
            } else {
                log.error("Netty server start up Error!");
            }
            //destroy();
        }
    }

    @PreDestroy
    public void destroy(){
        log.info("Shutdown Netty Server...");
        if(channel != null) { channel.close();}
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("Shutdown Netty Server Success!");
    }
}
