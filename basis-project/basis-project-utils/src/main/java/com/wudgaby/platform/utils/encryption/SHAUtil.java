package com.wudgaby.platform.utils.encryption;

import lombok.experimental.UtilityClass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description:主要功能:SHA-1 加密 不可逆（Secure Hash Algorithm，安全散列算法）
 * @Prject: CommonUtilLibrary
 * @Package: com.jingewenku.abrahamcaijin.commonutil.encryption
 * @author: AbrahamCaiJin
 * @date: 2017年05月16日 15:57
 * @Copyright: 个人版权所有
 * @Company:
 * @version: 1.0.0
 */
@UtilityClass
public class SHAUtil {

    /**
     * SHA-512 加密
     * @param data
     * @return
     */
    public static String encryptSHA512(byte[] data) {
        return encryptSHA(data, "SHA-512");
    }

    /**
     * 256位
     * @param data
     * @return
     */
    public static String encryptSHA256(byte[] data) {
        return encryptSHA(data, "SHA-256");
    }

    public static String encryptSHA(byte[] data, String algorithm) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance(algorithm);
            sha.update(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] resultBytes = sha.digest();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < resultBytes.length; i++) {
            if (Integer.toHexString(0xFF & resultBytes[i]).length() == 1) {
                builder.append("0").append(
                        Integer.toHexString(0xFF & resultBytes[i]));
            } else {
                builder.append(Integer.toHexString(0xFF & resultBytes[i]));
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        for(int i=0;i<2;i++){
            System.out.println(SHAUtil.encryptSHA512("123456789".getBytes()));
        }
    }
}