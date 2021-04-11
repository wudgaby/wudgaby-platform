package com.wudgaby.platform.flowable.helper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author WudGaby
 * @since 2020-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FlowNode对象", description="")
public class FlowNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "流程配置id")
    private Long flowId;

    @ApiModelProperty(value = "节点名称")
    private String nodeName;

    @ApiModelProperty(value = "节点key")
    private String nodeKey;

    @ApiModelProperty(value = "节点类型")
    private String nodeType;

    @ApiModelProperty(value = "分配id列表")
    private String assignIds;

    @ApiModelProperty(value = "分配名称列表")
    private String assignName;

    @ApiModelProperty(value = "审批人类型")
    private String apportionType;

    @ApiModelProperty(value = "多人审批时类型")
    private String signType;

    @ApiModelProperty(value = "等级,LEAD类型时必填")
    private Integer level;

    @ApiModelProperty(value = "表达式,EXPR类型时必填")
    private String expr;

    @ApiModelProperty(value = "允许选择其他抄送人")
    private Boolean allowOptional;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "前节点id")
    private Long prevId;

    @ApiModelProperty(value = "父节点id")
    private String prevIds;

    @ApiModelProperty(value = "层级")
    private Integer hierarchy;
}
