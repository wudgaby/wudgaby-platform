package com.wudgaby.starter.sign.service;

import com.wudgaby.sign.api.SignConst;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/19 18:28
 * @Desc :
 */
public class DefaultSignatureService implements SignatureService {
    @Override
    public String getAppSecret(String appId) {
        return SignConst.DEFAULT_SECRET;
    }
}
