package com.wudgaby.platform.flowable.helper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  ButtonsType {
    COMPLETE("提交"),

    STOP("终止"),

    ASSIGN("转派"),

    DELEGATE("委派"),

    BACK("退回"),

    TAKE_BACK("撤回");

    String value;
}
