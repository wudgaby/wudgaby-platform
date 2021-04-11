package com.wudgaby.platform.sys.entity;

import com.wudgaby.platform.core.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 资源表
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysResource对象", description="资源表")
public class SysResource extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "父id 0:根节点")
    private Long parentId;

    @ApiModelProperty(value = "父级ids ,分隔")
    private String parentIds;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "等级")
    private Integer level;

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
    private Integer status;

    @ApiModelProperty(value = "权限标识")
    private String permCode;

    @ApiModelProperty(value = "前端图标")
    private String icon;

    @ApiModelProperty(value = "前端组件")
    private String component;

    @ApiModelProperty(value = "前端样式")
    private String className;


}
