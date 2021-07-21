package com.wudgaby.platform.message.api.form;

import com.wudgaby.platform.message.api.enmus.SmsTmplEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @ClassName : SmsForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/10 10:48
 * @Desc :
 */
@Data
@ApiModel("短信模板表单")
public class SmsTmplForm implements Serializable {
    @ApiModelProperty(value = "群发手机号", required = true)
    @NotEmpty(message = "不能为空")
    @Size(max = 200, message = "最多支持200个手机号")
    private String[] phoneNums;

    @ApiModelProperty(value = "短信模板",  required = true)
    @NotNull(message = "模板不能为空")
    private SmsTmplEnum smsTmpl;

    @ApiModelProperty(value = "模板参数", required = true)
    private String[] params;
}
