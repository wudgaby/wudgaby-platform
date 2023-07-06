package com.wudgaby.platform.permission.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统资源-菜单信息
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BaseMenu对象", description="系统资源-菜单信息")
public class BaseMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单Id")
    @TableId(value = "menu_id", type = IdType.ASSIGN_ID)
    private Long menuId;

    @ApiModelProperty(value = "父级菜单")
    private Long parentId;

    @ApiModelProperty(value = "菜单编码")
    private String menuCode;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "描述")
    private String menuDesc;

    @ApiModelProperty(value = "路径前缀")
    private String scheme;

    @ApiModelProperty(value = "请求路径")
    private String path;

    @ApiModelProperty(value = "菜单标题")
    private String icon;

    @ApiModelProperty(value = "打开方式:_self窗口内,_blank新窗口")
    private String target;

    @ApiModelProperty(value = "优先级 越小越靠前")
    private Long priority;

    @ApiModelProperty(value = "状态:0-无效 1-有效")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "保留数据0-否 1-是 不允许删除")
    private Integer isPersist;

    @ApiModelProperty(value = "服务名")
    private String serviceId;


}
