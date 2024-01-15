package com.wudgaby.starter.resubmit;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/12/28 0028 14:57
 * @Desc :
 */
public interface ResubmitService {
    /**
     * 获取登录用户id
     */
    String getLoggedUserId();

    /**
     * 校验是否重复提交
     * @param key
     * @param message
     * @param expiresSec
     */
    void check(String key, String message, long expiresSec);
}
