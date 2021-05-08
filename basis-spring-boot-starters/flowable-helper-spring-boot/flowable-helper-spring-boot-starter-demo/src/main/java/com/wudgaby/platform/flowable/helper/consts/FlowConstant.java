package com.wudgaby.platform.flowable.helper.consts;

/**
 * @author : bruce.liu
 * @title: : FlowConstant
 * @projectName : flowable
 * @description: 常量
 * @date : 2019/11/1314:00
 */
public class FlowConstant {

    /**
     * 流程发起人
     */
    public static final String FLOW_INITIATOR_VAR = "initiator";
    public static final String FLOW_BIZKEY_VAR = "businessKey";
    public static final String PROCESS_DEF_VAR = "processDefKey";

    public static final String FLOW_INITIATOR_DEF_KEY = "initiateUserTask";

    /**
     * 提交人的变量名称
     */
    public static final String FLOW_SUBMITTER_VAR = "submitter";
    /**
     * 提交人节点名称
     */
    public static final String FLOW_SUBMITTER = "提交人";

    public static final String FLOW_USER_TASK_SUBMITTER_KEY = "usertask_initiate";
    /**
     * 自动跳过节点设置属性
     */
    public static final String FLOWABLE_SKIP_EXPRESSION_ENABLED = "_FLOWABLE_SKIP_EXPRESSION_ENABLED";
    /**
     * 挂起状态
     */
    public static final int SUSPENSION_STATE = 2;
    /**
     * 激活状态
     */
    public static final int ACTIVATE_STATE = 1;
    //后加签
    public static final String AFTER_ADDSIGN = "after";
    //前加签
    public static final String BEFORE_ADDSIGN = "before";

    /**
     * 动态流程
     */
    public static final String DYNAMIC_FLOW = "dynamic_flow";

    /**
     * 警情流程定义key
     */
    public static final String CASE_PROCESS_DEFINITION_KEY = "case_flow_global";
    /**
     * 处置单流程定义key
     */
    public static final String DISPOSE_PROCESS_DEFINITION_KEY = "dispose_flow_global";

    /**
     * 租户
     */
    public static final String TENANT = "";

    /**
     * 审批参数
     */
    public static final String OUTCOME = "outcome";
    /**
     * 投票结果
     */
    public static final String VOTE_RESULT = "vote_result";
    /**
     * 投票
     */
    public static final String VOTE = "vote_task:";

    /**
     * runtimeService.deleteProcessInstance 会出发监听器
     */
    public static final String ACTIVITY_DELETED = "ACTIVITY_DELETED";

    public static final String FLOW_LOG = "FLOW_LOG";

    public static final String VAR_TASK_SKIP_KEY = "taskSkip";

    public static final String VAR_ASSIGNEE_LIST_KEY = "_assigneeList";
}
