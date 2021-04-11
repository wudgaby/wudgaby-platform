package com.wudgaby.platform.flowable.helper.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : bruce.liu
 * @title: : TaskVo
 * @projectName : flowable
 * @description: 任务VO
 * @date : 2019/11/1315:11
 */
@Data
public class TaskQueryVo implements Serializable {

    /**
     * 用户工号
     */
    private String userCode;
}
