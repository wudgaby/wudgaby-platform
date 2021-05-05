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
@ApiModel("字典项表单")
public class DictItemForm {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "类型名称", required = true)
    @NotBlank(message = "类型名称必填")
    private String type;

    @ApiModelProperty(value = "字典项名称", required = true)
    @NotBlank(message = "字典项名称必填")
    private String dictName;

    @ApiModelProperty(value = "字典项值", required = true)
    @NotBlank(message = "字典项值必填")
    private String dictVal;

    @ApiModelProperty(value = "状态")
    private int status;

    @ApiModelProperty(value = "排序")
    private int sort;

    @ApiModelProperty(value = "备注")
    private String remark;
}