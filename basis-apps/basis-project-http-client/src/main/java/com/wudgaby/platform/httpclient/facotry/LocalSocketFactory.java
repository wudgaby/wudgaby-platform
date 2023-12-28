package com.wudgaby.platform.httpclient.facotry;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author wudgaby
 * @version V1.0
 
 * @Description: SOCKET工厂.可以选择不同IP出口.
 * @date 2018/9/27 14:53
 */
public class LocalSocketFactory extends SocketFactory {
	private InetAddress localAddress;
	private SocketFactory systemFactory = SocketFactory.getDefault();

	public LocalSocketFactory(InetAddress localAddress){
		this.localAddress = localAddress;
	}
	
	@Override
	public Socket createSocket() throws IOException {
		Socket socket = systemFactory.createSocket();
		socket.bind(new InetSocketAddress(localAddress, 0));
		return socket;
	}
	
	@Override
	public Socket createSocket(String host, int port) throws IOException{
		return systemFactory.createSocket(host, port, localAddress, 0);
	}

	@Override
	public Socket createSocket(InetAddress address, int port) throws IOException {
		return systemFactory.createSocket(address, port, localAddress, 0);
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localAddr, int localPort)
			throws IOException {
		return systemFactory.createSocket(host, port, localAddr, localPort);
	}

	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddr, int localPort) 
			throws IOException {
		return systemFactory.createSocket(address, port, localAddr, localPort);
	}
	
	public static SocketFactory byName(String ipOrInterface) throws SocketException {
	    InetAddress localAddress;
	    try {
	      // example 192.168.0.51
	      localAddress = InetAddress.getByName(ipOrInterface);
	    } catch (UnknownHostException uhe) {
	      // example en0
	      NetworkInterface networkInterface = NetworkInterface.getByName(ipOrInterface);
	      localAddress = networkInterface.getInetAddresses().nextElement();
	    }
	    return new LocalSocketFactory(localAddress);
	}
}
