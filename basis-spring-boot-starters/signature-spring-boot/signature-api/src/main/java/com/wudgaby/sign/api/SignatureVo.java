package com.wudgaby.sign.api;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/21 2:19
 * @Desc :
 */
@Data
@Accessors(chain = true)
public class SignatureVo {
    private String httpMethod;
    private String contentType;
    private String contentMD5;

    private SignatureHeaders signatureHeaders;
    private String path;
    private Map<String, String[]> params;
    private String secret;
}
