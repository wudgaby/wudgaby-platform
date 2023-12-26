package com.wudgaby.starter.datascope.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/8/2 0002 10:57
 * @desc :
 */
@AllArgsConstructor
@Getter
public enum DataScopeEnum {
    /**所有*/ALL("ALL"),
    /**部门*/DEPT("DEPT"),
    /**部门及以下*/DEPT_AND_CHILD("DEPT_AND_CHILD"),
    /**自己*/MYSELF("MYSELF"),
    /**自定义*/CUSTOM("CUSTOM"),
    ;


    private final String name;
}
