package com.wudgaby.platform.utils;

import lombok.experimental.UtilityClass;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/10/14 18:35
 * @Desc :
 */
@UtilityClass
public class BeanUtils {
    /**
     * 平台统一使用该方法进行转换.
     * 后期更换时,很方便.
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target){
        org.springframework.beans.BeanUtils.copyProperties(source, target);
    }
}
