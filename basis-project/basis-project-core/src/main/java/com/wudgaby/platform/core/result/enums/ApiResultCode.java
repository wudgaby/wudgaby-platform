package com.wudgaby.platform.core.result.enums;

import java.io.Serializable;

/**
 * @ClassName : ApiResultCode
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2018/9/26/026 22:02
 * @Desc :
 */
public interface ApiResultCode extends Serializable {
    int getCode();
    String getReason();
}
