package com.wudgaby.platform.message.api.form;

import com.wudgaby.platform.message.api.enmus.AliDeviceType;
import com.wudgaby.platform.message.api.enmus.AliPushType;
import com.wudgaby.platform.message.api.enmus.AliTargetType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/11/18 10:57
 * @Desc :   
 */
@Data
@Accessors(chain = true)
@ApiModel("阿里推送表单")
public class AliPushForm {
    @ApiModelProperty(value = "标题", required = true)
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "消息内容", required = true)
    @NotBlank(message = "消息内容不能为空")
    private String content;

    @ApiModelProperty(value = "目标类型", required = true)
    private AliTargetType targetType;

    @ApiModelProperty(value = "目标列表", required = false)
    private List<String> targetList;

    @ApiModelProperty(value = "附加信息", required = false)
    private Map<String, String> extrasMap;

    @ApiModelProperty(value = "推送类型", required = true)
    private AliPushType pushType;

    @ApiModelProperty(value = "设备类型", required = true)
    private AliDeviceType deviceType;
}
