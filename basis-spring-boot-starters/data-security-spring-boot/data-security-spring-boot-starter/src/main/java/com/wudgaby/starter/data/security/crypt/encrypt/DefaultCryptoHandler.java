package com.wudgaby.starter.data.security.crypt.encrypt;

import cn.hutool.core.util.StrUtil;
import com.wudgaby.starter.data.security.util.AESUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wudgaby
 */
@Slf4j
@Data
public class DefaultCryptoHandler implements CryptoHandler {
    @Override
    public String encrypt(String value) {
        if(StrUtil.isEmpty(value)){
            return value;
        }
        return AESUtil.encryptBase64(value);
    }

    @Override
    public String decrypt(String value) {
        if (StrUtil.isEmpty(value)) {
            return "";
        }
        try{
            return AESUtil.decrypt(value);
        }catch (Exception ex) {
            log.error("{} 解密失败. 返回原文.", value);
            return value;
        }
    }
}