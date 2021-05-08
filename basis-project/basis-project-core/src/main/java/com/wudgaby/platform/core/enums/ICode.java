package com.wudgaby.platform.core.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * @ClassName : ICode
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/10 9:48
 * @Desc :   枚举接口
 */
public interface ICode<T> {
    /**
     * Code
     * @return
     */
    T getCode();

    /**
     * name
     * @return
     */
    String getName();

    static <E extends Enum<?> & ICode, T extends Object> Optional<E> codeOf(Class<E> enumClass, T code) {
        return Arrays.stream(enumClass.getEnumConstants()).filter(e -> e.getCode() == code).findAny();
    }
}
