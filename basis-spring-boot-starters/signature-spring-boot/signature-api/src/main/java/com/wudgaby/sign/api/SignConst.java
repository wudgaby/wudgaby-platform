package com.wudgaby.sign.api;

import java.util.concurrent.TimeUnit;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/19 18:23
 * @Desc :
 */
public interface SignConst {
    String RESUBMIT_KEY = "sign:resubmit:nonce:";
    String DEFAULT_SECRET = "default_secret";
    Long EXPIRE_TIME = TimeUnit.MINUTES.toMillis(5);
    Long RESUBMIT_DURATION = TimeUnit.MINUTES.toSeconds(5);
}
