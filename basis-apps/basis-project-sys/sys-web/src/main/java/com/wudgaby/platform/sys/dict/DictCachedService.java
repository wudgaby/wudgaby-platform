package com.wudgaby.platform.sys.dict;

import cn.hutool.core.lang.tree.Tree;

import java.util.List;
import java.util.Optional;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/2 0002 18:22
 * @desc :
 */
public interface DictCachedService {
    /**
     * 启动时,初始化至缓存
     */
    void init2Cached();

    /**
     * 定时任务刷新至缓存
     */
    void refresh();

    /**
     * 获取字典类型列表
     * @return
     */
    List<DictDTO> listDictTypes();

    /**
     * 通过字典类型 和 字典项值 获取字典项
     * @param type
     * @param value
     * @return
     */
    Optional<DictDTO> getDictItemByVal(String type, String value);

    /**
     * 通过字典类型 和 字典项名称 获取字典项
     * @param type
     * @param label
     * @return
     */
    Optional<DictDTO> getDictItemByLabel(String type, String label);

    /**
     * 通过字典类型获取 字典项列表
     * @param type
     * @return
     */
    List<DictDTO> listDictItems(String type);

    /**
     * 获取字典项树结构
     * @param type
     * @return
     */
    List<Tree<Long>> treeDictItems(String type);
}
