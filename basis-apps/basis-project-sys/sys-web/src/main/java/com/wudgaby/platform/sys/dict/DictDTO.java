package com.wudgaby.platform.sys.dict;

import lombok.Data;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/2 0002 17:27
 * @desc :
 */
@Data
public class DictDTO {
    /**
     * 字典id
     */
    private Integer id;
    /**
     * 字典父id
     */
    private Integer pid;
    /**
     * 字典标签
     */
    private String label;
    /**
     * 字典值
     */
    private String value;
    /**
     * 字典类型
     */
    private String dictType;
}
