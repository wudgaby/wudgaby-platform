package com.wudgaby.platform.sys.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName : NoticeForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/10/9 18:11
 * @Desc :   TODO
 */
@Data
@ApiModel("公告表单")
public class NoticeForm {

    @ApiModelProperty(value = "状态 0:正常, 1:停用")
    private Integer status;

    @ApiModelProperty(value = "置顶0:不置顶,1:置顶")
    private Boolean stick;

    @NotBlank(message = "请输入标题")
    @ApiModelProperty(value = "标题", required = true)
    private String title;

    @NotBlank(message = "请输入内容")
    @ApiModelProperty(value = "内容", required = true)
    private String content;
}
