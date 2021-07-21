package com.wudgaby.platform.flowable.helper.enums;

import com.wudgaby.platform.core.enums.ICode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName : ProcessType
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/25 14:53
 * @Desc :   
 */
@Getter
@AllArgsConstructor
public enum ProcessType implements ICode<String> {
    /**
     * 
     */
    CASE_PROCESS("案情流程", "1"),
    DISPOSE_PROCESS("处置单流程", "2"),
    OTHER("其他", "");

    private String desc;
    private String defKey;

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getName() {
        return name();
    }


}
