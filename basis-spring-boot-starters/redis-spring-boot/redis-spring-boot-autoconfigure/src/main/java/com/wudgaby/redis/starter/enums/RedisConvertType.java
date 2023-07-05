package com.wudgaby.redis.starter.enums;

import lombok.Getter;

/**
 * @ClassName : RedisConvertType
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/16 14:40
 * @Desc :   redis key value 序列化类型
 */
@Getter
public enum RedisConvertType {
    /**
     * 序列化类型
     */
    //JAVA("java"),
    JACKSON("jackson"),
    //FASTJSON("fastjson"),
    STRING("string");

    private String desc;

    RedisConvertType(String desc) {
        this.desc = desc;
    }
}
