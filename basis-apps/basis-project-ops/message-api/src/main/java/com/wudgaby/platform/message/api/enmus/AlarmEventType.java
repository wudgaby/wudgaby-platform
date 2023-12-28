package com.wudgaby.platform.message.api.enmus;

import com.wudgaby.platform.core.enums.ICode;
import lombok.Getter;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/7/10 9:41
 * @Desc :   告警类型
 */
@Getter
public enum AlarmEventType implements ICode<String> {
    /*** 定义 */
    SYSTEM( "系统"),
    BUSINESS( "业务"),
    ACCOUNT( "账号"),
    PAYMENT( "充值"),
    ;

    private String desc;

    AlarmEventType(String desc){
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return desc;
    }

    @Override
    public String getName() {
        return name();
    }


}
