package com.wudgaby.platform.flowable.helper.vo;


import lombok.Data;

import java.util.Map;

/**
 * @author : bruce.liu
 * @title: : CompleteTaskVo
 * @projectName : flowable
 * @description: 执行任务Vo
 * @date : 2019/11/1315:27
 */
@Data
public class CompleteTaskVo extends BaseProcessVo {
    /**
     * 流程变量
     * 整个流程能看到
     * 持久化
     * 会被覆盖
     */
    private Map<String, Object> variables;

    /**
     * 任务变量
     * 与当前任务有关
     * 其他任务看不到
     * 不会被覆盖
     * 持久化
     */
    private Map<String, Object> localVariables;

    /**
     * 瞬时变量
     * 整个流程能看到
     * 不持久化
     * 会被覆盖
     */
    private Map<String, Object> transientVariables;
}
