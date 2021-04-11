package com.wudgaby.platform.flowable.helper.form;

import com.wudgaby.platform.flowable.helper.enums.ProcessType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName : DeployForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/3/2 10:22
 * @Desc :   TODO
 */
@Data
@ApiModel("部署流程表单")
public class DeployForm {
    @ApiModelProperty(value = "流程类型", required = true)
    @NotNull(message = "流程类型不能为空")
    private ProcessType processType;

    @ApiModelProperty(value = "流程名称", required = true)
    @NotBlank(message = "流程名称不能为空")
    private String processName;

    @ApiModelProperty(value = "流程文件路径", required = true)
    @NotBlank(message = "流程文件路径不能为空")
    private String fileUrl;

    @ApiModelProperty(value = "文件名称", required = true)
    @NotBlank(message = "文件名称不能为空")
    private String fileName;
}
