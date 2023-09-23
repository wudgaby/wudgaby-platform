package com.wudgaby.starter.enums.scanner.model;

import lombok.Data;

import java.util.List;

/**
 * @author: zhuCan
 * @date: 2020/7/9 11:25
 * @description: 枚举码表缓存的数据结构
 */
@Data
public class CodeTable {

    /**
     * 枚举名称
     */
    private String enumName;

    /**
     * 枚举里面的所有枚举值
     */
    private List<CodeItem> items;

    /**
     * 默认项
     */
    private CodeItem defaultItem;

    /**
     * 枚举的classPath,用来区分同名的枚举
     */
    private String classPath;


    public CodeTable(String enumName, List<CodeItem> items, CodeItem defaultItem, String classPath) {
        this.enumName = enumName;
        this.items = items;
        this.defaultItem = defaultItem;
        this.classPath = classPath;
    }
}
