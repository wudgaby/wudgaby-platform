package com.wudgaby.platform.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wudgaby.platform.core.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysDept对象", description="部门表")
public class SysDept extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "父id 0:根节点", required = true)
    private Long parentId;

    @ApiModelProperty(value = "父级ids ,分隔")
    private String parentIds;

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "部门名称", required = true)
    private String deptName;

    @ApiModelProperty(value = "负责人id")
    private Long deptLeader;

    @ApiModelProperty(value = "状态 0:正常, 1:停用")
    private Integer status;

    @ApiModelProperty(value = "乐观锁")
    private Integer version;

    //@ApiModelProperty(value = "逻辑删除 0:存在 1:删除")
    //private Boolean deleted;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "所属机构", required = true)
    private Long orgId;


}
