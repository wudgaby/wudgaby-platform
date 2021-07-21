package com.wudgaby.platform.flowable.helper.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName : TaskCompleteForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/21 11:29
 * @Desc :
 */
@Data
@ApiModel("撤销表单")
public class RejectForm {
    @ApiModelProperty(value = "流程实例id", required = true)
    @NotBlank(message = "流程实例id必填")
    private String processInsId;

    /*@ApiModelProperty(value = "撤销原因", required = true)
    @NotBlank(message = "撤销原因必填")
    private String comment;*/
}
