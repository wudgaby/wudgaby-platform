package com.wudgaby.plugin.resubmit.redis;

import com.wudgaby.plugin.resubmit.ResubmitException;
import com.wudgaby.plugin.resubmit.ResubmitService;
import com.wudgaby.redis.api.RedisSupport;
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
