package com.wudgaby.starter.enums.scanner.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: zhuCan
 * @date: 2020/7/9 11:25
 * @description: 枚举的数据值, 包括数值 code 和 标识名 name, 支持重写
 */
@Data
@Accessors(chain = true)
public class CodeItem {

    /**
     * code
     */
    private Object code;

    /**
     * name 标识
     */
    private String name;

    public CodeItem(){}

    public CodeItem(Object code, String name) {
        this.code = code;
        this.name = name;
    }
}
