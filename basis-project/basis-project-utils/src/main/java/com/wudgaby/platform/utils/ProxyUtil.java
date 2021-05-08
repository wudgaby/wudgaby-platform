package com.wudgaby.platform.utils;

import lombok.experimental.UtilityClass;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @ClassName : ProxyUtil
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/8 10:24
 * @Desc :   TODO
 */
@UtilityClass
public class ProxyUtil {
    public static String getProxyAddress(Proxy proxy){
        if(proxy == null){
            return "";
        }
        return ((InetSocketAddress)proxy.address()).getAddress().getHostAddress();
    }

    public static int getProxyPort(Proxy proxy){
        if(proxy == null){
            return -1;
        }
        return ((InetSocketAddress)proxy.address()).getPort();
    }
}
