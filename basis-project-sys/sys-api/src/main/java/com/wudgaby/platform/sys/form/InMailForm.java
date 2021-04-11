package com.wudgaby.platform.sys.form;

import com.wudgaby.platform.sys.enums.InMailType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("通知表单")
public class InMailForm {
    @ApiModelProperty(value = "通知标题",required = true)
    @NotBlank(message = "通知标题必填")
    @Length(max = 200, message = "标题最多200字")
    private String title;

    @ApiModelProperty(value = "通知内容",required = true)
    @NotBlank(message = "通知内容必填")
    private String content;

    @ApiModelProperty(value = "通知类型", required = true)
    @NotNull(message = "通知类型必填")
    private InMailType inMailType;

    @ApiModelProperty(value = "是否通知全员")
    private Boolean sendAll = false;

    @ApiModelProperty(value = "接收人id列表")
    private List<Long> receiverIds;

    @ApiModelProperty(value = "附件列表")
    private List<AttachmentForm> attachmentList;
}
