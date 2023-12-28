package com.wudgaby.platform.netty;

import com.wudgaby.platform.netty.netty.coder.ChineseMessage;
import com.wudgaby.platform.netty.netty.coder.ChineseMessageDecoder;
import com.wudgaby.platform.netty.netty.coder.ChineseMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/28 10:30
 * @Desc :
 */
@Slf4j
public class NettyClientTest {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.option(ChannelOption.TCP_NODELAY, Boolean.TRUE);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                    ch.pipeline().addLast(new LengthFieldPrepender(4));
                    ch.pipeline().addLast(new ChineseMessageEncoder());
                    ch.pipeline().addLast(new ChineseMessageDecoder());
                    ch.pipeline().addLast(new SimpleChannelInboundHandler<ChineseMessage>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, ChineseMessage message) throws Exception {
                            log.info("接收到服务端的响应:{}", message);
                        }
                    });
                }
            });
            ChannelFuture future = bootstrap.connect("localhost", 151001).sync();
            System.out.println("客户端启动成功...");
            Channel channel = future.channel();
            ChineseMessage message = new ChineseMessage();
            message.setId(1L);
            message.setMessage("张大狗");
            channel.writeAndFlush(message);
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
