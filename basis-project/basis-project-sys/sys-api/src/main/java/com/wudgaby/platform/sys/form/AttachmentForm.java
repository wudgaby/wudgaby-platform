package com.wudgaby.platform.sys.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("附件表单")
public class AttachmentForm {
    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件地址")
    private String fileUrl;
}
