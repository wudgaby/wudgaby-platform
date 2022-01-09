package com.wudgaby.platform.simplesecurity;

/**
 * @ClassName : SecurityUtils
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/29 9:48
 * @Desc :   
 */

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * @ClassName : SecurityUtils
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/7 17:31
 * @Desc :   
 */
@Slf4j
@UtilityClass
public class SecurityUtils {

    public LoginUser getCurrentUser() {
        return new LoginUser();
    }

}
