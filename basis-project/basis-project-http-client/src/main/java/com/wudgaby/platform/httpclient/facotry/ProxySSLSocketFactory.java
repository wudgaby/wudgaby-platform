package com.wudgaby.platform.httpclient.facotry;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @ClassName : ProxySSLSocketFactory
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/7/3 16:33
 * @Desc :   TODO
 */
public class ProxySSLSocketFactory extends SSLSocketFactory {
    private Proxy proxy;
    private SSLSocketFactory socketFactory;

    public ProxySSLSocketFactory(Proxy proxy, SSLSocketFactory socketFactory){
        this.proxy = proxy;
        this.socketFactory = socketFactory;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return socketFactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return socketFactory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(){
        if(proxy == null){
            return new Socket();
        }
        return new Socket(proxy);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean b) throws IOException {
        //TODO 无法代理
        return socketFactory.createSocket(socket, host, port, b);
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        Socket socket = createSocket();
        try {
            return socketFactory.createSocket(socket, host, port, true);
        } catch (IOException e) {
            socket.close();
            throw e;
        }
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress inetAddress, int i1) throws IOException, UnknownHostException {
        Socket socket = createSocket();
        try {
            socket.bind(new InetSocketAddress(inetAddress, i1));
            return socketFactory.createSocket(socket, host, port, true);
        } catch (IOException e) {
            socket.close();
            throw e;
        }
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int port) throws IOException {
        Socket socket = createSocket();
        try {
            return socketFactory.createSocket(socket, inetAddress.getHostAddress(), port, true);
        } catch (IOException e) {
            socket.close();
            throw e;
        }
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int port, InetAddress clientAddress, int clientPort) throws IOException {
        Socket socket = createSocket();
        try {
            socket.bind(new InetSocketAddress(clientAddress, clientPort));
            return socketFactory.createSocket(socket, inetAddress.getHostAddress(), port, true);
        } catch (IOException e) {
            socket.close();
            throw e;
        }
    }
}
