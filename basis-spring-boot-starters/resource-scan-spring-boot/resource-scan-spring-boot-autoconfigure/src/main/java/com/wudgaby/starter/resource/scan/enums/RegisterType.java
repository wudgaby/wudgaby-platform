package com.wudgaby.starter.resource.scan.enums;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/8/30 15:39
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
