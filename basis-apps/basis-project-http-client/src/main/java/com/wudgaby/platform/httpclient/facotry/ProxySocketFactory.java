package com.wudgaby.platform.httpclient.facotry;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

/**
 * @author wudgaby
 * @version V1.0
 
 * @Description: SOCKET工厂.可以选择不同IP出口.
 * @date 2018/9/27 14:53
 */
public class ProxySocketFactory extends SocketFactory {
	private Proxy proxy;

	public ProxySocketFactory(Proxy proxy){
		this.proxy = proxy;
	}
	
	@Override
	public Socket createSocket(){
		if(proxy == null){
			return new Socket();
		}
		return new Socket(proxy);
	}
	
	@Override
	public Socket createSocket(String host, int port) throws IOException{
		Socket socket = createSocket();
		try {
			socket.connect(new InetSocketAddress(host, port));
			return socket;
		} catch (IOException e) {
			socket.close();
			throw e;
		}
	}

	@Override
	public Socket createSocket(InetAddress address, int port) throws IOException {
		Socket socket = createSocket();
		try {
			socket.connect(new InetSocketAddress(address, port));
			return socket;
		} catch (IOException e) {
			socket.close();
			throw e;
		}
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localAddr, int localPort)
			throws IOException {
		Socket socket = createSocket();
		try {
			socket.bind(new InetSocketAddress(localAddr, localPort));
			socket.connect(new InetSocketAddress(host, port));
			return socket;
		} catch (IOException e) {
			socket.close();
			throw e;
		}
	}

	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddr, int localPort) 
			throws IOException {
		Socket socket = createSocket();
		try {
			socket.bind(new InetSocketAddress(localAddr, localPort));
			socket.connect(new InetSocketAddress(address, port));
			return socket;
		} catch (IOException e) {
			socket.close();
			throw e;
		}
	}
}
