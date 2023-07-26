package com.wudgaby.sample.data.sensitive.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/26 0026 17:51
 * @desc :
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {
    NORMAL(0, "正常"),
    ABNORMAL(1, "异常"),

    UNKNOWN(2, "未知"),
    ;

    private Integer status;
    private String name;

    public static StatusEnum get(int status) {
        StatusEnum[] statusEnums = StatusEnum.values();
        for (StatusEnum se : statusEnums) {
            if (se.status == status) {
                return se;
            }
        }
        return StatusEnum.UNKNOWN;
    }
}
