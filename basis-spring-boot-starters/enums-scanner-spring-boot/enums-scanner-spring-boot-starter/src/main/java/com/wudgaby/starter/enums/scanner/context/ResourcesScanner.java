package com.wudgaby.starter.enums.scanner.context;

import java.util.List;

/**
 * @author zhuCan
 * @description 资源扫描
 * @since 2020-12-21 17:27
 **/
public interface ResourcesScanner<T> {

    /**
     * 扫描资源列表
     *
     * @return resources
     */
    List<T> classScan();

}
