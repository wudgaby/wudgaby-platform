package com.wudgaby.platform.httpclient;

import lombok.Data;
import lombok.experimental.Accessors;

import java.net.Proxy;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2018/11/16/016 16:48
 * @Desc :
 */
@Data
@Accessors(chain = true)
public class ProxyConfigVo {
    private Proxy proxy;
    private String userName;
    private String password;

    private Long proxyId;
    private String publicIp;
    private String country;
}
