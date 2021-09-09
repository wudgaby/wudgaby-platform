package com.wudgaby.platform.netty.netty;

import com.alibaba.fastjson.JSON;
import com.wudgaby.platform.netty.vo.WsMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @ClassName : WsServerHandler
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/28 10:57
 * @Desc :   TextWebSocketFrame是netty用于处理websocket发来的文本对象
 */
@Slf4j
public class WsServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    /**
     * 所有正在连接的channel都会存在这里面，所以也可以间接代表在线的客户端
     */
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static volatile int online;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) throws Exception {
        //ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(msg)));
        log.info("收到客户端消息: {}", webSocketFrame);

        // 关闭请求
        if (webSocketFrame instanceof CloseWebSocketFrame) {
            for(Channel channel:channelGroup){
                if(channel == ctx.channel()){
                    channel.close();
                    return;
                }
            }
            return;
        }
        // ping请求
        if (webSocketFrame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(webSocketFrame.content().retain()));
            return;
        }
        // 只支持文本格式，不支持二进制消息
        if (!(webSocketFrame instanceof TextWebSocketFrame)) {
            log.error("仅支持文本(Text)格式，不支持二进制消息");
            return;
        }

        for(Channel channel:channelGroup){
            channel.writeAndFlush(new TextWebSocketFrame(((TextWebSocketFrame)webSocketFrame).text()));
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerAdded");

        channelGroup.add(ctx.channel());
        online = channelGroup.size();
        log.info(ctx.channel().remoteAddress()+" 上线了!");

        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerRemoved");

        channelGroup.remove(ctx.channel());
        online = channelGroup.size();
        log.info(ctx.channel().remoteAddress()+" 断开连接");

        super.handlerRemoved(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("userEventTriggered: {}", evt);
        //用于触发用户事件，包含触发读空闲、写空闲、读写空闲
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE){
                log.info("已经一段时间没有发送信息！");
            }

            if (event.state() == IdleState.READER_IDLE){
                log.info("已经一段时间没有读取到消息！");
            }

            if (event.state() == IdleState.ALL_IDLE) {
                log.info("已经一段时间没有读写消息！关闭连接");
                Channel channel = ctx.channel();
                //关闭无用channel，以防资源浪费
                channel.close();
            }
        }

        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelRegistered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelUnregistered");

        String name = (String)ctx.channel().attr(AttributeKey.valueOf("username")).get();
        WsMessage msg = new WsMessage();
        msg.setDate(new Date());
        msg.setName(name);
        msg.setMsg(name + " 离开了聊天室.");
        msg.setType(WsMessage.MsgType.NOTICE);
        msg.setOnlineNum(online);

        for(Channel channel:channelGroup){
            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(msg)));
        }

        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive");

        String name = "guest";
        WsMessage msg = new WsMessage();
        msg.setDate(new Date());
        msg.setName(name);
        msg.setMsg("欢迎 " + name + " 进入聊天室.");
        msg.setType(WsMessage.MsgType.NOTICE);
        msg.setOnlineNum(online);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(msg)));

        msg = new WsMessage();
        msg.setDate(new Date());
        msg.setName(name);
        msg.setMsg(name + " 进入聊天室");
        msg.setType(WsMessage.MsgType.NOTICE);
        msg.setOnlineNum(online);

        for(Channel channel:channelGroup){
            if(channel == ctx.channel()){
                continue;
            }
            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(msg)));
        }

        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("channelReadComplete");
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        log.info("channelWritabilityChanged");
        super.channelWritabilityChanged(ctx);
    }
}
