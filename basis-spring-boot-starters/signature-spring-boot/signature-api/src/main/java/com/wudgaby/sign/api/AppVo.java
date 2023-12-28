package com.wudgaby.sign.api;

import lombok.Data;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/12/1 15:17
 * @Desc :
 */
@Data
public class AppVo {
    private String id;
    private String appName;
    private String appId;
    private String appSecret;
    private long expiresIn;
    private String status;
}
