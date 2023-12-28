package com.wudgaby.platform.utils.encryption;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2018/9/19/019 22:31
 * @Desc :   
 */

import com.wudgaby.platform.utils.JavaBase64Util;
import lombok.experimental.UtilityClass;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @Description:主要功能:3DES对称加密（Triple DES、DESede，进行了三重DES加密的算法，对称加密算法）
 * @Prject: CommonUtilLibrary
 * @Package: com.jingewenku.abrahamcaijin.commonutil.encryption
 * @author: AbrahamCaiJin
 * @date: 2017年05月16日 15:58
 * @Copyright: 个人版权所有
 * @Company:
 * @version: 1.0.0
 */
@UtilityClass
public class TripleDESUtil {
    private static final String KEY_ALGORITHM = "DESede";
    /**
     * 默认的加密算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password) throws NoSuchAlgorithmException {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        //112 168
        kg.init(168, new SecureRandom(password.getBytes()));
        //生成一个密钥
        SecretKey secretKey = kg.generateKey();
        // 转换为AES专用密钥
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
    }

    /**
     * 3DES 加密
     */
    public static String encrypt(String content, String password) throws Exception {
        // 创建密码器
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        byte[] byteContent = content.getBytes("utf-8");
        // 初始化为加密模式的密码器
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));
        // 加密
        byte[] result = cipher.doFinal(byteContent);
        //通过Base64转码返回
        return JavaBase64Util.encode2String(result);
    }

    /**
     * 3DES 解密
     */
    public static String decrypt(String content, String password) throws Exception{
        //实例化
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        //使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
        //执行操作
        byte[] result = cipher.doFinal(JavaBase64Util.decode2Byte(content));
        return new String(result, "utf-8");
    }

    public static void main(String[] args) throws Exception {
        String pwd = "mykey";
        String d = TripleDESUtil.encrypt("1111111111", pwd);
        System.out.println(d);
        System.out.println(TripleDESUtil.decrypt(d, pwd));
    }
}