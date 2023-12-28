package com.wudgaby.platform.core.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/9/27 10:16
 * @Desc :   
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class FieldDiffVO {
    private String fieldName;
    private Boolean ignore;
    private Object oldVal;
    private Object newVal;
}
