package com.wudgaby.platform.permission.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/7/15 16:07
 * @Desc :
 */
@ApiModel("操作表单")
@Data
public class ActionForm {
    @ApiModelProperty(value = "资源ID", hidden = true)
    private Long actionId;

    @ApiModelProperty(value = "资源编码", required = true)
    @NotBlank(message = "请填写资源编码")
    private String actionCode;

    @ApiModelProperty(value = "资源名称", required = true)
    @NotBlank(message = "请填写资源名称")
    private String actionName;

    @ApiModelProperty(value = "资源描述")
    private String actionDesc;

    @ApiModelProperty(value = "资源父节点")
    private Long menuId;

    @ApiModelProperty(value = "优先级 越小越靠前")
    private Integer priority;

    @ApiModelProperty(value = "服务名称")
    private String serviceId;
}
