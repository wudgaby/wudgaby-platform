package com.wudgaby.platform.message.api.form;

import com.wudgaby.platform.message.api.enmus.MailTmplEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/7/10 12:18
 * @Desc :
 */
@Data
@ApiModel("模板邮件表单")
public class MailTmplForm implements Serializable {
    @ApiModelProperty(value = "收件地址")
    @NotEmpty(message = "收件地址不能为空")
    private String[] sendTo;

    @ApiModelProperty(value = "邮件模板")
    private MailTmplEnum mailTmplEnum;

    @ApiModelProperty(value = "模板参数")
    private String[] params;
}
