package com.wudgaby.platform.message.api.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/7/10 12:18
 * @Desc :   
 */
@Data
@ApiModel("普通邮件表单")
public class NormalMailForm implements Serializable {
    @ApiModelProperty(value = "收件地址")
    @NotEmpty(message = "收件地址不能为空")
    private String[] sendTo;

    @ApiModelProperty(value = "邮件标题")
    @NotBlank(message = "邮件标题不能为空")
    private String subject;

    @ApiModelProperty(value = "邮件内容")
    @NotBlank(message = "邮件内容不能为空")
    private String content;

    @ApiModelProperty(value = "是否是html内容")
    private boolean isHtml;
}
