package com.wudgaby.platform.flowable.helper.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName : BpmnModelVo
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/3/26 15:14
 * @Desc :   TODO
 */
@Data
@ApiModel("bpmnModel表单")
public class BpmnModelVo {
    public enum ApportionType{
        /**用户*/USER,
        /**组*///GROUP,
        /**角色*/ROLE,
        /**领导*/LEAD,
        /**表达式*/EXPR,
        ;
    }

    @ApiModelProperty(value = "节点显示名称")
    private String nodeName;

    @ApiModelProperty(value = "nodekey,暂时无用")
    private String userTaskKey;

    @ApiModelProperty(value = "分配人")
    private List<String> idList;

    @ApiModelProperty(value = "分配名称")
    private List<String> nameList;

    @ApiModelProperty(value = "分配类型", required = true)
    private ApportionType type;

    @ApiModelProperty(value = "等级,LEAD类型时必填")
    private Integer level;

    @ApiModelProperty(value = "表达式,EXPR类型时必填")
    private String expr;
}
