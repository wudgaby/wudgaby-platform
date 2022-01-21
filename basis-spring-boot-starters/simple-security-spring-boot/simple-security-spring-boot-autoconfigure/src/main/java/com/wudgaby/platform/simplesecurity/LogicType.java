package com.wudgaby.platform.simplesecurity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2022/1/20 0020 15:49
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

    private String desc;
}
