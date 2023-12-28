package com.wudgaby.platform.permission.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/7/15 16:31
 * @Desc :
 */
@ApiModel("接口表单")
@Data
public class ApiForm {
    @ApiModelProperty(value = "接口ID", hidden = true)
    private Long apiId;

    @ApiModelProperty(value = "接口编码", required = true)
    @NotBlank(message = "请填写接口编码")
    private String apiCode;

    @ApiModelProperty(value = "接口名称", required = true)
    @NotBlank(message = "请填写接口名称")
    private String apiName;

    @ApiModelProperty(value = "接口分类:default-默认分类")
    private String apiCategory;

    @ApiModelProperty(value = "资源描述")
    private String apiDesc;

    @ApiModelProperty(value = "请求方式")
    private String requestMethod;

    @ApiModelProperty(value = "响应类型")
    private String contentType;

    @ApiModelProperty(value = "服务ID")
    private String serviceId;

    @ApiModelProperty(value = "请求路径")
    private String path;

    @ApiModelProperty(value = "优先级")
    private Long priority;

    @ApiModelProperty(value = "是否需要认证: 0-无认证 1-身份认证 默认:1")
    private Integer isAuth;

    @ApiModelProperty(value = "是否公开: 0-内部的 1-公开的")
    private Integer isOpen;

    @ApiModelProperty(value = "类名")
    private String className;

    @ApiModelProperty(value = "方法名")
    private String methodName;
}
