package com.wudgaby.platform.netty.netty;

import com.wudgaby.platform.netty.consts.NettyContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

/**
 * @ClassName : HttpRequestHandler
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/28 10:06
 * @Desc :   TODO
 */
@Slf4j
public class HttpRequestHandler extends SimpleChannelInboundHandler<Object> {

    static final MethodHandle setHandshakerMethod = getSetHandshakerMethod();
    static final MethodHandle forbiddenHttpRequestResponderMethod = getForbiddenHttpRequestResponderMethod();

    static MethodHandle getSetHandshakerMethod(){
        try {
            Method method = WebSocketServerProtocolHandler.class.getDeclaredMethod("setHandshaker"
                    , Channel.class
                    , WebSocketServerHandshaker.class
            );
            method.setAccessible(true);

            return MethodHandles.lookup().unreflect(method);
        } catch (Throwable e) {
            // Should never happen
            e.printStackTrace();
            System.exit(5);
            return null;
        }
    }

    static MethodHandle getForbiddenHttpRequestResponderMethod(){
        try {
            Method method =  WebSocketServerProtocolHandler.class.getDeclaredMethod("forbiddenHttpRequestResponder");
            method.setAccessible(true);

            return MethodHandles.lookup().unreflect(method);
        } catch (Throwable e) {
            // Should never happen
            e.printStackTrace();
            System.exit(6);
            return null;
        }
    }

    /**
     * 描述：读取完连接的消息后，对消息进行处理。
     * 这里仅处理HTTP请求，WebSocket请求交给下一个处理器。
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            ctx.fireChannelRead(((WebSocketFrame) msg).retain());
        }
    }

    /**
     * 描述：处理Http请求，主要是完成HTTP协议到Websocket协议的升级
     * @param ctx
     * @param req
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req,
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        if (req.method() != HttpMethod.GET) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
            return;
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(ctx.pipeline(), req, NettyContext.WEBSOCKET_PATH), null, false, 65536, false);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
        NettyContext.webSocketHandshakerMap.put(ctx.channel().id().asLongText(), handshaker);

        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            /*String name = req.uri();
            ctx.channel().attr(AttributeKey.valueOf("name")).set(name);
            handshaker.handshake(ctx.channel(), req);*/

            Channel channel = ctx.channel();
            final ChannelFuture handshakeFuture = handshaker.handshake(channel, req, getResponseHeaders(req), channel.newPromise());
            handshakeFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        ctx.fireExceptionCaught(future.cause());
                    } else {
                        ctx.fireUserEventTriggered(
                                WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE);
                    }
                }
            });

            try {
                setHandshakerMethod.invokeExact(ctx.channel(), handshaker);

                ChannelHandler handler = (ChannelHandler)forbiddenHttpRequestResponderMethod.invokeExact();
                ctx.pipeline().replace(this, "WS403Responder", handler);
            } catch (Throwable e) {
                // Should never happen
                e.printStackTrace();
                System.exit(7);
            }
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        boolean keepAlive = HttpUtil.isKeepAlive(req);
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!keepAlive) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 描述：异常处理，关闭channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
        String protocol = "ws";
        if (cp.get(SslHandler.class) != null) {
            // SSL in use so use Secure WebSockets
            protocol = "wss";
        }
        return protocol + "://" + req.headers().get(HttpHeaderNames.HOST) + path;
    }

    private static HttpHeaders getResponseHeaders(FullHttpRequest req){
        final String cookieName = "cid";
        final DefaultHttpHeaders httpHeaders = new DefaultHttpHeaders();

        String connectionID = null;
        String cookieString = req.headers().get(HttpHeaderNames.COOKIE);
        if( cookieString != null && cookieString.length() > 0 )
        {
            Set<io.netty.handler.codec.http.cookie.Cookie> cookies = ServerCookieDecoder.LAX.decode(cookieString);
            for (io.netty.handler.codec.http.cookie.Cookie cookie : cookies) {
                if( cookieName.equalsIgnoreCase(cookie.name())){
                    connectionID = cookie.value();
                    break;
                }
            }
        }
        if( connectionID == null || connectionID.length() < 16 || connectionID.length() > 50 ){
            connectionID = UUID.randomUUID().toString().replaceAll("-", "");
        }


        io.netty.handler.codec.http.cookie.DefaultCookie cookie = new io.netty.handler.codec.http.cookie.DefaultCookie(cookieName, connectionID);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);

        httpHeaders.add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.LAX.encode(cookie));
        return httpHeaders;
    }
}
