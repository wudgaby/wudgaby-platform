package com.wudgaby.platform.socketio.client;

import com.google.common.collect.Maps;
import com.wudgaby.platform.core.result.ApiResult;
import io.socket.client.Ack;
import io.socket.client.AckWithTimeout;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.*;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/3/5 0005 9:44
 * @desc :
 */
@Slf4j
@RestController
public class ClientTestController {

    private Socket socket;
    private Dispatcher dispatcher;

    @GetMapping("/connect")
    public ApiResult connect() {
        URI uri = URI.create("http://localhost:9092");

        Map<String, String> authMap = Maps.newHashMap();
        authMap.put("token", "abcd");

        //https://socketio.github.io/socket.io-client-java/initialization.html
        IO.Options options = IO.Options.builder()
                // IO 工厂选项
                .setForceNew(false) //是否创建新的 Manager 实例
                .setMultiplex(true) //与 forceNew 相反 ：是否重用现有 Manager 实例相反。

                // 低级引擎选项
                //.setTransports(new String[] {PollingXHR.NAME, Polling.NAME, WebSocket.NAME })  //可以通过以下方式建立与 Socket.IO 服务器的低级别连接
                .setUpgrade(true)  //客户端是否应尝试将传输从 HTTP 长轮询升级到更好的传输。
                .setRememberUpgrade(false) //如果为 true，并且之前与服务器的 WebSocket 连接成功，则连接尝试将绕过正常的升级过程，最初将尝试 WebSocket。传输错误后的连接尝试将使用正常的升级过程。建议您仅在使用 SSL/TLS 连接时打开此功能，或者如果您知道您的网络不会阻止 websocket。
                .setPath("/socket.io/") //它是在服务器端捕获的路径的名称.服务器和客户端值必须匹配. 请注意，这与 URI 中的路径不同，后者表示命名空间
                .setQuery("x=42&y=abc") //其他查询参数（然后在服务器端的对象中找到 socket.handshake.query ）. 该会话query全程不会变.
                .setExtraHeaders(null) //扩展请求头. 与上面的 query 选项类似，该 socket.handshake.headers 对象包含 Socket.IO 握手期间发送的标头，在当前会话期间不会更新，这意味着仅在当前会话关闭并创建新会话时，在客户端更改 extraHeaders 才有效

                // Manager options 管理器选项
                //这些设置将由附加到同一 Manager 的所有套接字实例共享
                .setReconnection(true) //是否启用重新连接。如果设置为 false ，则需要手动重新连接。
                .setReconnectionAttempts(Integer.MAX_VALUE) //放弃前的重新连接尝试次数。
                .setReconnectionDelay(1_000) //重新连接前的初始延迟（以毫秒为单位）（受 randomizationFactor 值影响）。
                .setReconnectionDelayMax(5_000) //两次重新连接尝试之间的最大延迟。每次尝试都会使重新连接延迟增加 2 倍。
                .setRandomizationFactor(0.5) //重新连接时使用的随机化因子（例如，客户端不会在服务器崩溃后在同一时间重新连接）。
                .setTimeout(20_000) //每次连接尝试的超时时间（以毫秒为单位）。

                // Socket options 套接字选项

                //.setAuth(Collections.singletonMap("token", "abcd")) //访问命名空间时发送的凭据
                .setAuth(authMap)
                .build();

        /*//用于 HTTP 长轮询请求的 OkHttpClient 实例。
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES) // important for HTTP long-polling
                .build();
        options.callFactory = okHttpClient;

        //用于 WebSocket 连接的 OkHttpClient 实例。
        OkHttpClient wsokHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                //.minWebSocketMessageToCompress(2048)
                .build();
        options.webSocketFactory = wsokHttpClient;*/

        //SSL connections SSL 连接
        /*OkHttpClient sslOkHttpClient = sslOkHttpClient();
        options.callFactory = sslOkHttpClient;
        options.webSocketFactory = sslOkHttpClient;*/

        // 如何创建大量客户端
        // 默认情况下，您不能创建超过 5 个 Socket.IO 客户端（任何其他客户端都将因“传输错误”或“ping 超时”原因断开连接）。
        // 这是由于默认的 OkHttp 调度程序，其 maxRequestsPerHost 默认设置为 5。
        int MAX_CLIENTS = 100;
        dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(MAX_CLIENTS * 2);
        dispatcher.setMaxRequestsPerHost(MAX_CLIENTS * 2);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .readTimeout(1, TimeUnit.MINUTES) // important for HTTP long-polling
                .build();
        options.callFactory = okHttpClient;
        options.webSocketFactory = okHttpClient;

        socket = IO.socket(uri, options);

        socket.on(Socket.EVENT_CONNECT, args -> {
            log.info("EVENT_CONNECT: {} - {}", socket.id(), socket.connected());
        });
        socket.on(Socket.EVENT_DISCONNECT, args -> {
            log.info("EVENT_DISCONNECT: {}- {}", socket.id(), socket.connected());
        });
        socket.on(Socket.EVENT_CONNECT_ERROR, args -> {
            log.info("EVENT_CONNECT_ERROR: {}", args);
            options.auth.put("authorization", "bearer 1234");
            socket.connect();
        });

        socket.on("hello", args -> {
           log.info("hello: {}", args[0]);
            if (args.length > 1 && args[1] instanceof Ack) {
                ((Ack) args[1]).call("hi!");
            }
        });

        socket.once("once", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                log.info("{}", args[0]);
            }
        });

        //包罗万象的侦听器, 对于传入数据包
        socket.onAnyIncoming(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                log.info("onAnyIncoming: {}", args[0]);
            }
        });

        //包罗万象的侦听器, 对于传出数据包
        socket.onAnyOutgoing(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                log.info("onAnyOutgoing: {}", args[0]);
            }
        });

        socket.connect();
        return ApiResult.success(socket.connected() ? "已连接" : "未连接");
    }

    @GetMapping("/disConnect")
    public ApiResult disConnect() {
        if(socket == null){
            return ApiResult.failure("请先连接");
        }
        if(socket.connected()) {
            socket.disconnect();
            dispatcher.executorService().shutdown();
        }
        return ApiResult.success(socket.connected() ? "未断开" : "已断开");
    }

    @PostMapping("/send")
    public ApiResult sendMessage(@RequestParam String event, @RequestParam String message) {
        if(socket == null){
            return ApiResult.failure("请先连接");
        }

        if(socket.connected()) {
            socket.emit(event, message);
        }
        return ApiResult.success();
    }

    @PostMapping("/sendAck")
    public ApiResult sendAck(@RequestParam String event, @RequestParam String message) {
        if(socket == null){
            return ApiResult.failure("请先连接");
        }

        if(socket.connected()) {
            socket.emit("update item", 1, new JSONObject(Collections.singletonMap("name", "updated")), (Ack) args -> {
                //一旦对方确认该事件，就会调用此回调：
                JSONObject response = (JSONObject) args[0];
                try {
                    log.info(response.getString("status"));
                } catch (JSONException e) {
                    log.error(e.getMessage(), e);
                }
            });

            socket.emit("update item timeout", 1, new JSONObject(Collections.singletonMap("name", "updated")), new AckWithTimeout(5000) {
                @Override
                public void onTimeout() {
                    log.info("超时了");
                }

                @Override
                public void onSuccess(Object... args) {
                    log.info("{}", args.toString());
                }
            });
        }
        return ApiResult.success();
    }

    //SSL connections SSL 连接
    @SneakyThrows
    private OkHttpClient sslOkHttpClient(){
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession sslSession) {
                return hostname.equals("example.com");
            }
        };

        KeyStore ks = KeyStore.getInstance("JKS");
        File file = new File("keystore.jks");
        ks.load(Files.newInputStream(file.toPath()), "password".toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, "password".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .hostnameVerifier(hostnameVerifier)
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) tmf.getTrustManagers()[0])
                .readTimeout(1, TimeUnit.MINUTES) // important for HTTP long-polling
                .build();

        return okHttpClient;
    }

    @SneakyThrows
    private OkHttpClient trustAllCerts(){
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession sslSession) {
                return true;
            }
        };

        X509TrustManager trustManager = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
                // not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                // not implemented
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] { trustManager }, null);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .hostnameVerifier(hostnameVerifier)
                .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                .readTimeout(1, TimeUnit.MINUTES) // important for HTTP long-polling
                .build();

        return okHttpClient;
    }

}
