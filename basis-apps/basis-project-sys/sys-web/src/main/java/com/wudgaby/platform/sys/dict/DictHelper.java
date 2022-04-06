package com.wudgaby.platform.sys.dict;

import cn.hutool.core.lang.tree.Tree;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/2 0002 18:10
 * @desc :
 */
@Slf4j
public class DictHelper {
    private static DictCachedService dictCachedService;

    public static void init(DictCachedService dictCachedService){
        DictHelper.dictCachedService = dictCachedService;
        log.info("已完成初始化字典助手.");
    }

    public static List<DictDTO> listDictTypes() {
        return dictCachedService.listDictTypes();
    }

    public static DictDTO getDictItemByVal(String type, String value) {
        return dictCachedService.getDictItemByVal(type, value);
    }

    public static DictDTO getDictItemByLabel(String type, String label) {
        return dictCachedService.getDictItemByLabel(type, label);
    }

    public static List<DictDTO> listDictItems(String type) {
        return dictCachedService.listDictItems(type);
    }

    public static List<Tree<Long>> treeDictItems(String type) {
        return dictCachedService.treeDictItems(type);
    }
}
