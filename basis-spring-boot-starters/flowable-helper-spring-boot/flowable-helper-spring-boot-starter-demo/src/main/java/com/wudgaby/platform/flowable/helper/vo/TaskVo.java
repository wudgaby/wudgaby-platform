package com.wudgaby.platform.flowable.helper.vo;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : bruce.liu
 * @title: : TaskVo
 * @projectName : flowable
 * @description: 任务vo
 * @date : 2019/11/2316:09
 */
@Data
public class TaskVo implements Serializable {
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 审批人
     */
    private String approver;
    /**
     * 审批人id
     */
    private String approverId;
    /**
     * 表单名称
     */
    private String formName;
    /**
     * 业务主键
     */
    private String businessKey;
    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 开始时间
     */
    @JSONField(format = DatePattern.NORM_DATETIME_PATTERN)
    private Date startTime ;

    /**
     * 结束时间
     */
    @JSONField(format = DatePattern.NORM_DATETIME_PATTERN)
    private Date endTime;
    /**
     * 系统标识
     */
    private String systemSn;
}
