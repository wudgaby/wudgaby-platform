package com.wudgaby.apiautomatic.enums;

/**
 * @ClassName : ApiStatus
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/8/30 15:39
 * @Desc :   注册类型
 */
public enum RegisterType {
    /** redis set*/
    REDIS,

    /** redis 发布订阅模式 */
    REDIS_PUB_SUB,

    /**mq*/
    MQ,

    /**EVENT*/
    EVENT,
    ;
}
