package com.wudgaby.platform.core.enums;

import lombok.Getter;
import org.springframework.util.Assert;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2018/9/26/026 20:47
 * @Desc :
 */
@Getter
public enum EnvEnum {
    /**本地*/LOCAL,
    /**开发*/DEV,
    /**联调*/FE,
    /**测试*/TEST,
    /**生产*/PROD,
    ;

    public static boolean isProdEnv(String activeEnv) {
        Assert.notNull(activeEnv, "env parameter not null.");
        return EnvEnum.PROD.name().equalsIgnoreCase(activeEnv);
    }
}
