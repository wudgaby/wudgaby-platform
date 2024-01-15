package com.wudgaby.starter.redis.enums;

import lombok.Getter;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/7/16 14:40
 * @Desc :   redis key value 序列化类型
 */
@Getter
public enum RedisConvertType {
    /**
     * 序列化类型
     */
    JACKSON("jackson"),
    //FASTJSON("fastjson"),
    STRING("string");

    private String desc;

    RedisConvertType(String desc) {
        this.desc = desc;
    }
}
