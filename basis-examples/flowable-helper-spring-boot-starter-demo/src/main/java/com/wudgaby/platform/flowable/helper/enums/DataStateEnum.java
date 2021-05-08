package com.wudgaby.platform.flowable.helper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.wudgaby.platform.core.enums.ICode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataStateEnum implements ICode<Integer> {

    /***/
    NORMAL(0, "正常"),
    /**
     * 禁用或者屏蔽
     */
    SHIELDING(1, " 屏蔽"),

    DELETE(2, "删除");

    @EnumValue
    private Integer code;
    private String desc;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name();
    }


}
