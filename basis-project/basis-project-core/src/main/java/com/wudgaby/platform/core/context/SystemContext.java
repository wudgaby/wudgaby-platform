package com.wudgaby.platform.core.context;

import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/2/23 0023 12:24
 * @desc :
 */
public interface SystemContext {
    List<String> getDictCodesByDictType(String... dictType);
}
