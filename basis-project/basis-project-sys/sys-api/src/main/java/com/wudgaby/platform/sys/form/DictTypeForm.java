package com.wudgaby.platform.sys.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName : DictTypeForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/10/9 20:55
 * @Desc :   TODO
 */
@Data
@ApiModel("字典类型表单")
public class DictTypeForm {
    @ApiModelProperty(value = "类型名称", required = true)
    @NotBlank(message = "名称必填")
    private String name;

    @ApiModelProperty(value = "类型", required = true)
    @NotBlank(message = "类型必填")
    private String type;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "状态")
    private int status;

    @ApiModelProperty(value = "是否系统")
    private boolean isSys;

    @ApiModelProperty(value = "备注")
    private String remark;
}
