package com.wudgaby.platform.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wudgaby.platform.core.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字典类型表
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysDictType对象", description="字典类型表")
public class SysDictType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字典类型名称")
    private String name;

    @ApiModelProperty(value = "字典类型")
    private String type;

    @ApiModelProperty(value = "状态 0:正常, 1:停用")
    private Integer status;

    @ApiModelProperty(value = "乐观锁")
    private Integer version;

   /* @ApiModelProperty(value = "逻辑删除 0:存在 1:删除")
    private Boolean deleted;*/

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否系统")
    @TableField("is_sys")
    private Boolean sys;

}
