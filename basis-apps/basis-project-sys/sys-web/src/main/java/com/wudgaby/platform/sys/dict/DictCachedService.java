package com.wudgaby.platform.sys.dict;

import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/2 0002 18:22
 * @desc :
 */
public interface DictCachedService {
    void init2Cached();

    void refresh();

    List<DictDTO> listDictTypes();

    DictDTO getDictItemByVal(String type, String value);

    DictDTO getDictItemByLabel(String type, String label);

    List<DictDTO> listDictItems(String type);
}
