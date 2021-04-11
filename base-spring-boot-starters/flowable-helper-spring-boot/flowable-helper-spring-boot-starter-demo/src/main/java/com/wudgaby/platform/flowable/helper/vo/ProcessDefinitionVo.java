package com.wudgaby.platform.flowable.helper.vo;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName : ProcessDefinitionVo
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/18 17:00
 * @Desc :   TODO
 */
@Data
@Accessors(chain = true)
public class ProcessDefinitionVo {
    @ApiModelProperty("流程定义id")
    protected String id;
    @ApiModelProperty("流程key")
    protected String modelKey;
    @ApiModelProperty("前端定义的流程名称")
    protected String name;
    @ApiModelProperty("xml里的定义的流程名称")
    protected String processName;
    @ApiModelProperty("流程版本号")
    protected int version;
    @ApiModelProperty("类别")
    protected String category;
    @ApiModelProperty("部署id")
    protected String deploymentId;
    @ApiModelProperty("资源名称")
    protected String resourceName;
    @ApiModelProperty("流程名称")
    protected String dgrmResourceName;
    @ApiModelProperty("状态")
    protected int suspensionState;
    @ApiModelProperty("租户")
    protected String tenantId;
    @ApiModelProperty("部署时间")
    @JSONField(format = DatePattern.NORM_DATETIME_PATTERN)
    protected Date deployTime;
}
