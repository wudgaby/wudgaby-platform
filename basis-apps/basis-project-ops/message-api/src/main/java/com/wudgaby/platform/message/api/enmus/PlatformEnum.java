package com.wudgaby.platform.message.api.enmus;

import com.wudgaby.platform.core.enums.ICode;
import lombok.Getter;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/7/10 9:41
 * @Desc :   平台
 */
@Getter
public enum PlatformEnum implements ICode<String> {
    /*** 定义 */
    NONE("unknown"),
    PC( "pc"),
    IOS( "ios"),
    ANDROID( "android"),
    MOBILE("mobile"),
    MINI_APP("mini app")
    ;

    private String desc;

    PlatformEnum(String desc){
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
