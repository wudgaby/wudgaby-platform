package com.wudgaby.platform.flowable.helper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.wudgaby.platform.core.enums.ICode;
import com.wudgaby.swagger.configuration.SwaggerDisplayEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName : AuditState
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/22 10:19
 * @Desc :
 */
@Getter
@AllArgsConstructor
@SwaggerDisplayEnum
public enum AuditState implements ICode<Integer> {
    /**
     * 待审核
     */
    WAIT_AUDIT(0, "待审核"),

    /**
     * 审核中, 数据库中不放该状态. 搜索使用以及响应使用
     */
    AUDIT_ING(1, "审核中"),

    /**
     * 通过
     */
    AUDIT_PASS(2, "审核通过"),
    /**
     * 拒绝
     */
    AUDIT_REJECT(3, "审核不通过"),
    /**
     * 撤销
     */
    AUDIT_REPEAL(4, "已撤销"),
    /**
     * 已审核所有, 数据库中不放该状态. 搜索使用
     */
    ALL(5, "所有")
    ;

    @EnumValue
    private int state;
    private String desc;

    @Override
    public Integer getCode() {
        return state;
    }

    @Override
    public String getName() {
        return name();
    }

}
