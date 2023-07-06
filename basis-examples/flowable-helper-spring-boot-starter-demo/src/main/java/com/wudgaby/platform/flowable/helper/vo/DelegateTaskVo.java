package com.wudgaby.platform.flowable.helper.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : bruce.liu
 * @title: : DelegateTaskVo
 * @projectName : flowable
 * @description: 委派
 * @date : 2019/11/1315:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DelegateTaskVo extends BaseProcessVo {
    /**
     * 委派人
     */
    private String delegateUserCode;
}
