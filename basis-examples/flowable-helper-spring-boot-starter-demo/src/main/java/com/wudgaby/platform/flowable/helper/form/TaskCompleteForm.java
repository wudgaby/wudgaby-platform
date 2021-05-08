package com.wudgaby.platform.flowable.helper.form;

import com.wudgaby.platform.flowable.helper.enums.AuditType;
import com.wudgaby.platform.flowable.helper.vo.CcToVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @ClassName : TaskCompleteForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/21 11:29
 * @Desc :   TODO
 */
@Data
@ApiModel("任务办理表单")
public class TaskCompleteForm {
    @ApiModelProperty(value = "任务id", required = true)
    @NotBlank(message = "审批任务id必填")
    private String taskId;

    @ApiModelProperty(value = "审批类型", required = true)
    @NotNull(message = "审批类型必填")
    private AuditType auditType;

    @ApiModelProperty(value = "审批意见", required = false)
    @Length(max = 200, message = "审批意见请输入200字以内.")
    private String comment;

    @ApiModelProperty(value = "抄送人", required = false)
    private Set<CcToVo> ccToVos;
}
