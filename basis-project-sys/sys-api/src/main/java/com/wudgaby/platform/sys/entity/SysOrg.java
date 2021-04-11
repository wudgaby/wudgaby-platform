package com.wudgaby.platform.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wudgaby.platform.core.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 机构表
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysOrg对象", description="机构表")
public class SysOrg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "机构全称")
    private String fullName;

    @ApiModelProperty(value = "机构简称")
    private String abbrName;

    @ApiModelProperty(value = "机构代号")
    private String orgCode;

    @ApiModelProperty(value = "所属区域")
    private Integer area;

    @ApiModelProperty(value = "状态 0:正常, 1:停用")
    private Integer status;

    @ApiModelProperty(value = "乐观锁")
    private Integer version;

    @ApiModelProperty(value = "逻辑删除 0:存在 1:删除")
    private Boolean deleted;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "父id 0:根节点")
    private Long parentId;

    @ApiModelProperty(value = "父级ids ,分隔")
    private String parentIds;

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "负责人")
    private String leader;

    @ApiModelProperty(value = "负责人电话")
    private String phone;

    @ApiModelProperty(value = "负责人邮箱")
    private String email;

    @ApiModelProperty(value = "负责人地址")
    private String address;

    @ApiModelProperty(value = "备注")
    private String remark;


}
