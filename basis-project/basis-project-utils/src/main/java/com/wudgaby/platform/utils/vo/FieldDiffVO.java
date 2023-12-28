package com.wudgaby.platform.utils.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/9/27 10:16
 * @Desc :   TODO
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class FieldDiffVO {
    private String fieldName;
    private Boolean ignore;
    private Object oldVal;
    private Object newVal;
}
