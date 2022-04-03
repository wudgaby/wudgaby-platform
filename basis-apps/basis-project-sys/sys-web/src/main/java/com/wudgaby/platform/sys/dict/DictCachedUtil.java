package com.wudgaby.platform.sys.dict;

import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.Map;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/2 0002 18:10
 * @desc :
 */
public class DictCachedUtil {
    private static Map<String, DictDTO> dictTypeMap;
    private static Map<String, List<DictDTO>> dictParentMap;

    public static DictDTO getDictByVal(String type, String value) {
        List<DictDTO> dictDTOList = dictParentMap.get(type);
        return dictDTOList.stream().filter(dictDTO -> StrUtil.equals(dictDTO.getValue(), value)).findFirst().get();
    }

    public static DictDTO getDictByLabel(String type, String label) {
        List<DictDTO> dictDTOList = dictParentMap.get(type);
        return dictDTOList.stream().filter(dictDTO -> StrUtil.equals(dictDTO.getLabel(), label)).findFirst().get();
    }

    public static List<DictDTO> listDict(String type) {
        return dictParentMap.get(type);
    }
}
