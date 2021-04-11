package com.wudgaby.platform.message.api.enmus;

import lombok.Getter;

/**
 * @ClassName : TargetType
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/12/20 15:03
 * @Desc :   TODO
 */
@Getter
public enum AliTargetType {
    /**
     * 按设备推送
     */
    DEVICE,
    /**
     * 按别名推送
     */
    ALIAS,
    /**
     * 按帐号推送
     */
    ACCOUNT,
    /**
     * 按标签推送
     */
    TAG,
    /**
     * 广播推送
     */
    ALL
}
