package com.wudgaby.platform.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wudgaby.platform.core.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 访问日志表
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysLog对象", description="访问日志表")
public class SysLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "操作人id")
    private Long userId;

    @ApiModelProperty(value = "操作人名称")
    private String userName;

    @ApiModelProperty(value = "操作名称")
    private String action;

    @ApiModelProperty(value = "描述")
    @TableField(value = "`desc`")
    private String desc;

    @ApiModelProperty(value = "客户端user-agent")
    private String userAgent;

    @ApiModelProperty(value = "访问地址")
    private String reqUrl;

    @ApiModelProperty(value = "访问方法")
    private String reqMethod;

    @ApiModelProperty(value = "请求参数")
    private String reqParam;

    @ApiModelProperty(value = "请求头")
    private String reqHeaders;

    @ApiModelProperty(value = "请求ip")
    private String reqIp;

    @ApiModelProperty(value = "请求区域")
    private String reqRegion;

    @ApiModelProperty(value = "服务器ip")
    private String serverAddr;

    @ApiModelProperty(value = "异常信息")
    private String error;

    @ApiModelProperty(value = "请求时间")
    private Date reqTime;

    @ApiModelProperty(value = "响应时间")
    private Date respTime;

    @ApiModelProperty(value = "执行时间ms")
    private Integer runTime;

    @ApiModelProperty(value = "日志类型SYSTEM")
    private String type;

    @ApiModelProperty(value = "系统名称")
    private String os;

    @ApiModelProperty(value = "浏览器名称")
    private String browser;

    @ApiModelProperty(value = "是否成功0:成功 1:失败")
    @TableField("is_succeed")
    private Boolean succeed;

    @ApiModelProperty(value = "返回消息")
    private String response;

    @ApiModelProperty(value = "响应状态")
    private String httpStatus;


}
