package com.wudgaby.platform.message.api.enmus;

import com.wudgaby.platform.core.enums.ICode;
import lombok.Getter;

/**
 * @ClassName : SmsTmplEnum
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/10 9:41
 * @Desc :   告警级别
 */
@Getter
public enum AlarmLevelEnum implements ICode<String> {
    /*** 定义 */
    HINT( "提示"),
    GENERAL( "一般"),
    IMPORTANCE( "重要"),
    EXIGENCY( "紧急"),
    ;

    private String desc;

    AlarmLevelEnum(String desc){
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
