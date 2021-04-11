package com.wudgaby.locks.starter.api.enums;

import lombok.Getter;

/**
 * @ClassName : LockType
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/25 15:24
 * @Desc :   分布式锁类型
 */
@Getter
public enum LockType {
    /**
     * 可重入锁
     */
    REENTRANT,
    /**
     * 公平锁
     */
    FAIR,
    /**
     * 联锁
     */
    MULTI,
    /**
     * 红锁
     */
    RED,
    /**
     * 读锁
     */
    READ,
    /**
     * 写锁
     */
    WRITE,
    ;
}
