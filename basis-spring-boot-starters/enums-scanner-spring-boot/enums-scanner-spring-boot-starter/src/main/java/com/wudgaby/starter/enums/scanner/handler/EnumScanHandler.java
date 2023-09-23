package com.wudgaby.starter.enums.scanner.handler;

import com.wudgaby.starter.enums.scanner.model.CodeTable;

import java.util.List;

/**
 * @author: zhuCan
 * @date: 2020/7/9 11:48
 * @description: 获取枚举码表的接口
 */
public interface EnumScanHandler {

    /**
     * 获取所有的枚举接口
     *
     * @return 获取所有枚举码表
     */
    List<CodeTable> codeTables();

}
