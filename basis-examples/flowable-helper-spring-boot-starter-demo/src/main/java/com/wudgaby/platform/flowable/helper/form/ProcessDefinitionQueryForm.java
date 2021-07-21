package com.wudgaby.platform.flowable.helper.form;

import com.wudgaby.platform.core.model.form.DateRangeForm;
import com.wudgaby.platform.flowable.helper.enums.ProcessType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName : ProcessDefinitionQueryForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/25 14:59
 * @Desc :
 */
@ApiModel("流程定义查询表单")
@Data
public class ProcessDefinitionQueryForm extends DateRangeForm {
    @ApiModelProperty("流程类型")
    private ProcessType processType;
}
