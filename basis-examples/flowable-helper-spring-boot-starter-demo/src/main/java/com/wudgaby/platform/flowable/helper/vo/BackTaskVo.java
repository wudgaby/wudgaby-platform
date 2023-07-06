package com.wudgaby.platform.flowable.helper.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 驳回的实体VO
 * @Author: Bruce.liu
 * @Since:9:19 2018/9/8
 * 爱拼才会赢 2018 ~ 2030 版权所有
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BackTaskVo extends BaseProcessVo {

    /**
     * 需要驳回的节点id 必填
     */
    private String distFlowElementId;
}
