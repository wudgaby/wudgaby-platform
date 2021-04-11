package com.wudgaby.platform.httpclient;

import lombok.Data;
import lombok.experimental.Accessors;

import java.net.Proxy;

/**
 * @ClassName : ProxyConfigVo
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2018/11/16/016 16:48
 * @Desc :   TODO
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
