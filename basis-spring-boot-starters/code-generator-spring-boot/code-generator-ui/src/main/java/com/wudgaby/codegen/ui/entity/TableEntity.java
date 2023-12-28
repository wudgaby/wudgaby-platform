package com.wudgaby.codegen.ui.entity;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/7/6/006 22:28
 * @Desc :   
 */
@Data
@Accessors(chain = true)
public class TableEntity implements Serializable {
    private String tableName;
    private String engine;
    private String tableComment;
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date createTime;
}
