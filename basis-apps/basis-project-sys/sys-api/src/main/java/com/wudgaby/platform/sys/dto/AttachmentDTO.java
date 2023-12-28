package com.wudgaby.platform.sys.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/9/28 18:02
 * @Desc :
 */
@Data
@ApiModel("附件传输对象")
public class AttachmentDTO {
    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件地址")
    private String fileUrl;
}
