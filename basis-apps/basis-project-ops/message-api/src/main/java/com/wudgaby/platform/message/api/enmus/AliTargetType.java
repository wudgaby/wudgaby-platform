package com.wudgaby.platform.message.api.enmus;

import lombok.Getter;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/12/20 15:03
 * @Desc :
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
