package com.wudgaby.platform.flowable.helper.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : bruce.liu
 * @title: : TurnTaskVo
 * @projectName : flowable
 * @description: 转办Vo
 * @date : 2019/11/1315:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TurnTaskVo extends BaseProcessVo {

    /**
     * 被转办人工号 必填
     */
    private String turnToUserId;
}
