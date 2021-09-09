package com.wudgaby.platform.sys.dto;

import com.wudgaby.platform.sys.entity.SysInMail;
import com.wudgaby.platform.sys.form.AttachmentForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName : InMailDTO
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/9/28 18:02
 * @Desc :
 */
@Data
@Accessors(chain = true)
@ApiModel("通知公告传输对象")
public class InMailDTO {
    @ApiModelProperty("通知消息")
    private SysInMail sysInMail;
    @ApiModelProperty("附件")
    private List<AttachmentForm> attachmentList;
    @ApiModelProperty("接收人列表")
    private List<String> receiverNameList;
}
