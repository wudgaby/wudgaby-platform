package com.wudgaby.platform.flowable.helper.vo.flow;

import lombok.Data;

import java.util.List;

/**
 * @ClassName : NodeConfigVo
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/12/7 11:49
 * @Desc :   审批节点配置
 */
@Data
public class NodeConfigVo {
    public enum NodeType{
        /**审批*/ APPROVAL,
        /**抄送*/ CC,
        /**网关*/ GATEWAY,
        ;
    }

    /**
     * 审批人类型
     */
    public enum ApprovalType{
        /**用户*/ USER,
        /**角色*/ ROLE,
        /**领导*/ LEAD,

        /**组*/ GROUP,
        /**表达式*/ EXPR,
        ;
    }

    /**
     * 多人审批时类型
     */
    public enum SignType{
        /**会签 所有审批人通过*/ AND_SIGN,
        /**或签 一名审批人通过或拒绝即可*/ OR_SIGN,
        ;
    }

    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 节点显示名称
     */
    private String nodeName;

    /**
     *nodeKey
     */
    private String nodeKey;

    /**
     * 节点类型
     */
    private NodeType nodeType;

    /**
     * 分配人
     */
    private List<String> idList;

    /**
     * 显示名称
     */
    private List<String> nameList;

    /**
     * 审批人类型
     */
    private ApprovalType apportionType;

    /**
     * 多人审批时类型
     */
    private SignType signType;

    /**
     * 等级,LEAD类型时必填
     */
    private Integer level;

    /**
     * 表达式,EXPR类型时必填
     */
    private String expr;

    /**0.
     * 允许选择其他抄送人
     */
    private Boolean allowOptional;

    /**
     * 串行/并行
     */
    private Boolean sequential = false;

    /**
     * 父节点id
     */
    private String prevNodeId;

    //private List<NodeConfigVo> branches;

    /**
     * 标题
     */
    //private String title;
    /**
     * 优先级
     */
    //private int priority;
    /**
     * 条件
     */
    //private List<String> conditions;
    /**
     * 默认流
     */
    //private Boolean defaultFlow = false;
}
