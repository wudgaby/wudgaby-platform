package com.wudgaby.platform.message.api.form;

import com.wudgaby.platform.core.validation.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName : SmsForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/10 10:48
 * @Desc :   TODO
 */
@Data
@ApiModel("短信发送表单")
public class SmsForm implements Serializable {
    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    @Phone
    private String phoneNum;

    @ApiModelProperty(value = "验证码", required = true)
    @NotBlank(message = "验证码不能为空")
    private String captcha;

    //不支持自定义内容发送
    /*@ApiModelProperty(value = "短信内容")
    private String message;*/
}
