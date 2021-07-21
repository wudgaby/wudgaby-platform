package com.wudgaby.platform.twofactorauth.sample.vo;

import lombok.Data;

/**
 * @ClassName : User
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/12/9 23:09
 * @Desc :
 */
@Data
public class User {
    private String account;
    private String password;
    private String secretKey;
    private Long googleCode;
}
