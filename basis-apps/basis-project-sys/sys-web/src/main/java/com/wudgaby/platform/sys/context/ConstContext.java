package com.wudgaby.platform.sys.context;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/29 0029 14:21
 * @desc : 常量内存缓存
 */
public class ConstContext {
    private static final Dict CONST_CONTEXT = Dict.create();

    public static void put(String key, Object val) {
        if(ObjectUtil.hasEmpty(key, val)){
            return;
        }
        CONST_CONTEXT.put(key, val);
    }

    public static void get(String key) {
        CONST_CONTEXT.get(key);
    }

    public static void remove(String key) {
        CONST_CONTEXT.remove(key);
    }

    public static Dict me(){
        return CONST_CONTEXT;
    }
}
