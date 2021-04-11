package com.wudgaby.platform.flowable.helper.vo;

import com.wudgaby.platform.flowable.helper.enums.CommentTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author : bruce.liu
 * @title: : StartProcessVo
 * @projectName : flowable
 * @description: 启动流程VO
 * @date : 2019/11/1314:50
 */
@Data
public class StartProcessInstanceVo implements Serializable {

    /**
     * 流程定义key 必填
     */
    private String processDefinitionKey;
    /**
     * 业务系统id 必填
     */
    private String businessKey;
    /**
     * 启动流程变量 选填
     */
    private Map<String, Object> variables;
    /**
     * 申请人工号 必填
     */
    private String currentUserCode;
    /**
     * 系统标识 必填
     */
    private String systemSn;
    /**
     * 表单显示名称 必填
     */
    private String formName;
    /**
     * 流程提交人工号 必填
     */
    private String creator;

    /**
     * 意见类型
     */
    private CommentTypeEnum commentTypeEnum;
}
