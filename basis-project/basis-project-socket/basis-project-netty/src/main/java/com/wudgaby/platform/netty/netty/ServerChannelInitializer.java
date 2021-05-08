package com.wudgaby.platform.netty.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName : ServerChannelInitializer
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/28 10:07
 * @Desc :   TODO
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline= socketChannel.pipeline();

        pipeline.addLast(new IdleStateHandler(60, 60, 120, TimeUnit.SECONDS));
        //以下三个是Http的支持
        //http解码器
        pipeline.addLast(new HttpServerCodec());
        //支持写大数据流 以块的方式来写的处理器
        pipeline.addLast(new ChunkedWriteHandler());
        //http聚合器
        pipeline.addLast(new HttpObjectAggregator(1024*62));

        //websocket支持,设置路由
        //pipeline.addLast(new WebSocketServerProtocolHandler("/websocket/netty"));

        pipeline.addLast(new HttpRequestHandler());
        pipeline.addLast(new WsServerHandler());


        /**
         * 通用粘包解决方案 https://blog.csdn.net/u010853261/article/details/55803933
         */
        /*pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast(new HttpRequestHandler());*/
    }
}
