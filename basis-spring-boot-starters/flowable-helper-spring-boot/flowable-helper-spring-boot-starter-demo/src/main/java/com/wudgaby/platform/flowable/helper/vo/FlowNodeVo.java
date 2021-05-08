package com.wudgaby.platform.flowable.helper.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : bruce.liu
 * @title: : FlowNodeVo
 * @projectName : flowable
 * @description: 流程节点的Vo
 * @date : 2019/12/616:24
 */
@Data
public class FlowNodeVo implements Serializable {
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 执行人的code
     */
    private String userCode;
    /**
     * 执行人姓名
     */
    private String userName;

    /**
     * 任务节点结束时间
     */
    private Date endTime;
}
