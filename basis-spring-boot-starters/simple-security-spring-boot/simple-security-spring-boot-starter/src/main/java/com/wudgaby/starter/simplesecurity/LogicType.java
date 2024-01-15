package com.wudgaby.starter.simplesecurity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2022/1/20 0020 15:49
 * @Desc :  逻辑类型
 */
@Getter
@AllArgsConstructor
public enum LogicType {
    /**
     * 或
     */
    OR("或者"),
    /**
     * 与
     */
    AND("并且")
    ;

    private final String desc;
}
