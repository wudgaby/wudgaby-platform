package com.wudgaby.platform.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wudgaby.platform.core.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysRole对象", description="角色")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "描述")
    private String roleDesc;

    @ApiModelProperty(value = "状态 0:正常, 1:停用")
    private Integer status;

    //@ApiModelProperty(value = "逻辑删除 0:存在 1:删除")
    //private Boolean deleted;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否系统级别 1:是,0否")
    @TableField("is_sys")
    private Boolean sys;


}
