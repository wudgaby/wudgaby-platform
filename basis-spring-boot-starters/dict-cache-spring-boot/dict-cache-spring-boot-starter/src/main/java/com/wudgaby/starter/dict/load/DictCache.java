package com.wudgaby.starter.dict.load;

import cn.hutool.core.lang.tree.Tree;
import com.wudgaby.starter.dict.api.DictVO;

import java.util.List;
import java.util.Optional;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/11/2 0002 17:23
 * @desc :
 */
public interface DictCache {
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
    List<DictVO> listDictTypes();

    /**
     * 通过字典项列表
     * @return
     */
    List<DictVO> listDictItems();

    /**
     * 通过字典类型 和 字典项值 获取字典项
     * @param type
     * @param value
     * @return
     */
    Optional<DictVO> getDictItemByVal(String type, String value);

    /**
     * 通过字典类型 和 字典项名称 获取字典项
     * @param type
     * @param label
     * @return
     */
    Optional<DictVO> getDictItemByLabel(String type, String label);

    /**
     * 通过字典类型获取 字典项列表
     * @param type
     * @return
     */
    List<DictVO> listDictItemsByType(String type);

    /**
     * 获取字典项树结构
     * @param type
     * @return
     */
    List<Tree<Object>> treeDictItemsByType(String type);
}
