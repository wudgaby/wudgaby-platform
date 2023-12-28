package com.wudgaby.oauth2.uaa.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/10/9 12:07
 * @Desc :
 */
@JsonSerialize(using = CustomOAuth2ExceptionSerializer.class)
public class CustomerOAuth2Exception extends OAuth2Exception {
    public CustomerOAuth2Exception(String msg) {
        super(msg);
    }
}
