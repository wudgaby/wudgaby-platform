package com.wudgaby.scanner;

import com.wudgaby.starter.enums.scanner.annotation.EnumScan;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/9/23 0023 14:17
 * @desc :
 */
@EnumScan(defaultCode = "2", bindCode = "val", bindName = "label")
@Getter
@AllArgsConstructor
public enum StatusEnum {
    NORMAL(0, "正常"),
    ABNORMAL(1, "异常"),
    UNKNOWN(2, "未知"),

    ;

    private Integer val;
    private String label;
}
