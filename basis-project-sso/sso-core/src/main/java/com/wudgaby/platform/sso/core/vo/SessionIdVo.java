package com.wudgaby.platform.sso.core.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName : SessionIdVo
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/24 9:44
 * @Desc :   TODO
 */
@Data
@Accessors(chain = true)
public class SessionIdVo {
    private String storeKey;
    private String version;
}
