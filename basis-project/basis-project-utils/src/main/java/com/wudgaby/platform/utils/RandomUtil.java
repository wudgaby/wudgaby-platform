package com.wudgaby.platform.utils;

import lombok.experimental.UtilityClass;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName : RandomUtil
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/8/22 19:36
 * @Desc :
 */
@UtilityClass
public class RandomUtil {
    /**
     * 使用hutool的RandomUtil
     * @param min
     * @param max
     * @return
     */
    public static int randomRange(int min, int max){
        //return new Random().nextInt((max - min) + 1) + min;
        return getRandom().nextInt(min, max);
    }

    /**
     * hreadLocalRandom是JDK 7之后提供并发产生随机数，能够解决多个线程发生的竞争争夺。
     * @return
     */
    public static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }

    /**
     * 类提供加密的强随机数生成器
     * @return
     */
    public static SecureRandom getSecureRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
