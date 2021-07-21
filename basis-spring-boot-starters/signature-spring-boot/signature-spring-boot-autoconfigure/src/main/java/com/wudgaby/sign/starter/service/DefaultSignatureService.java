package com.wudgaby.sign.starter.service;

import com.wudgaby.sign.api.SignConst;

/**
 * @ClassName : DefaultSignatureService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/19 18:28
 * @Desc :
 */
public class DefaultSignatureService implements SignatureService {
    @Override
    public String getAppSecret(String appId) {
        return SignConst.DEFAULT_SECRET;
    }
}
