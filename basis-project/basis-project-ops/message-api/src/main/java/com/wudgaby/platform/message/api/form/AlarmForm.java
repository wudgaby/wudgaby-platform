package com.wudgaby.platform.message.api.form;

import com.wudgaby.platform.message.api.enmus.AlarmEventType;
import com.wudgaby.platform.message.api.enmus.AlarmLevelEnum;
import com.wudgaby.platform.message.api.enmus.PlatformEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName : AlarmForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/10 12:56
 * @Desc :
 */
@Data
@ApiModel("告警表单")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmForm implements Serializable {
    @ApiModelProperty(value = "服务名称", required = true)
    @NotBlank(message = "服务名称不能为空")
    private String serviceName;

    @ApiModelProperty(value = "标题", required = true)
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "等级", required = true)
    @NotNull(message = "等级不能为空")
    private AlarmLevelEnum level;

    @ApiModelProperty(value = "请求平台", required = true)
    @NotNull(message = "平台不能为空")
    private PlatformEnum platform;

    @ApiModelProperty(value = "事件类型", required = true)
    @NotNull(message = "事件类型不能为空")
    private AlarmEventType eventType;

    @ApiModelProperty(value = "内容", required = true)
    @NotBlank(message = "内容不能为空")
    private String content = "无错误描述";

    @ApiModelProperty(value = "请求IP", required = true)
    @NotBlank(message = "请求IP不能为空")
    private String ip;
}
