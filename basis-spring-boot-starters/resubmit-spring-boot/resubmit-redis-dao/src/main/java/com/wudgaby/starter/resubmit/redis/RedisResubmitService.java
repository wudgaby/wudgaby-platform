package com.wudgaby.starter.resubmit.redis;

import com.wudgaby.starter.redis.support.RedisSupport;
import com.wudgaby.starter.resubmit.ResubmitException;
import com.wudgaby.starter.resubmit.ResubmitService;
import lombok.AllArgsConstructor;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/12/28 0028 14:39
 * @desc :
 */
@AllArgsConstructor
public class RedisResubmitService implements ResubmitService {
    private final RedisSupport redisSupport;

    @Override
    public String getLoggedUserId() {
        return "";
    }

    @Override
    public void check(String key, String message, long expiresSec) {
        Object val = redisSupport.get(key);
        if (val == null) {
            redisSupport.set(key, key, expiresSec);
        } else {
            throw new ResubmitException(message);
        }
    }
}
