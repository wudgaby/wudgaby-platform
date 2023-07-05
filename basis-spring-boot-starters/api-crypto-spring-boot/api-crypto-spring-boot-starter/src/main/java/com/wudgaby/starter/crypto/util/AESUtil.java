package com.wudgaby.starter.crypto.util;

import cn.hutool.crypto.symmetric.SymmetricCrypto;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/5 0005 14:34
 * @desc :
 */
public class AESUtil {
    private static SymmetricCrypto aes;

    public static void init(SymmetricCrypto aes){
        AESUtil.aes = aes;
    }

    /**
     * Hex加密
     * 体积大,解码编码速度快
     *
     */
    public static String encryptHex(byte[] data) {
        return aes.encryptHex(data);
    }
    public static String encryptHex(InputStream data) {
        return aes.encryptHex(data);
    }
    public static String encryptHex(String data) {
        return aes.encryptHex(data, StandardCharsets.UTF_8);
    }

    /**
     * base64加密
     * 体积小，解码编码速度比较慢。
     */
    public static String encryptBase64(byte[] data) {
        return aes.encryptBase64(data);
    }
    public static String encryptBase64(InputStream data) {
        return aes.encryptBase64(data);
    }
    public static String encryptBase64(String data) {
        return aes.encryptBase64(data, StandardCharsets.UTF_8);
    }

    /**
     * 解密Hex（16进制）或Base64表示的字符串
     */
    public static String decrypt(byte[] data) {
        return aes.decryptStr(data, StandardCharsets.UTF_8);
    }
    public static String decrypt(InputStream data) {
        return aes.decryptStr(data);
    }
    public static String decrypt(String data) {
        return aes.decryptStr(data, StandardCharsets.UTF_8);
    }
}
