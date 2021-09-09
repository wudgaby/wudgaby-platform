package com.wudgaby.platform.sso.server.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 资源表
 * </p>
 *
 * @author jimi
 * @since 2020-11-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_resource")
@ApiModel(value="Resource对象", description="资源表")
public class Resource implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "resource_id", type = IdType.AUTO)
    private Integer resourceId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "修改者")
    private String updateBy;

    @ApiModelProperty(value = "父id 0:根节点")
    private Integer parentId;

    @ApiModelProperty(value = "资源路径 ,分隔")
    private String resourcePath;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "资源名称")
    private String resName;

    @ApiModelProperty(value = "资源url")
    private String resUrl;

    @ApiModelProperty(value = "资源类型. CATALOG,MENU,BUTTON")
    private String resType;

    @ApiModelProperty(value = "请求方法")
    private String resMethod;

    @ApiModelProperty(value = "打开方式. BLANK(新窗口),CURRENT(当前窗口)")
    private String target;

    @ApiModelProperty(value = "状态 0:正常, 1:停用")
    private int resourceStatus;

    @ApiModelProperty(value = "权限标识")
    private String permCode;

    @ApiModelProperty(value = "前端图标")
    private String icon;

    @ApiModelProperty(value = "前端组件")
    private String component;

    @ApiModelProperty(value = "前端样式")
    private String className;

    @ApiModelProperty(value = "前端路由")
    private String route;

    @ApiModelProperty(value = "系统标识")
    private String sysCode;


}
