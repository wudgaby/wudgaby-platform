package com.wudgaby.scanner;

import com.wudgaby.starter.enums.scanner.annotation.EnumScan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.annotation.Description;

/**
 * @program: anti
 * @description: 性别枚举
 * @author: pengwei
 * @create :2021/3/17 16:50
 */
@Getter
@AllArgsConstructor
@Description("性别枚举")
@EnumScan(bindName = "desc")
public enum SexEnum {
    SEX_UNKNOWN(0, "未知"),
    SEX_MAN(1, "男"),
    SEX_WOMAN(2, "女"),
    ;

    private final int code;
    private final String name;
}
