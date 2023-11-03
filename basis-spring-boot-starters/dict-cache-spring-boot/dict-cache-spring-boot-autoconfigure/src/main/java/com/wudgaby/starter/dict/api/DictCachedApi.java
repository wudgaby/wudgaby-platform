package com.wudgaby.starter.dict.api;

import java.util.Date;
import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/2 0002 18:22
 * @desc :
 */
public interface DictCachedApi {
    /**
     * 获取字典类型列表
     * @return
     */
    List<DictVO> listDictTypes();

    /**
     * 通过字典项列表
     * @return
     */
    List<DictVO> listDictItems();

    /**
     * dict type是否有更新
     * @return
     */
    boolean hasUpdateForType(Date lastUpdatedTime);

    /**
     * dict item是否有更新
     * @return
     */
    boolean hasUpdateForItem(Date lastUpdatedTime);
}
