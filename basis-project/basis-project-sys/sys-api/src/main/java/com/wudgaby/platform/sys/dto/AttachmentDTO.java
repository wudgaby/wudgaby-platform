package com.wudgaby.platform.sys.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName : AttachmentDTO
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/9/28 18:02
 * @Desc :   TODO
 */
@Data
@ApiModel("附件传输对象")
public class AttachmentDTO {
    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件地址")
    private String fileUrl;
}
