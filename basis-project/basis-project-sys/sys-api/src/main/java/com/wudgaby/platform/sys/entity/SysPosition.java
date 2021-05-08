package com.wudgaby.platform.sys.entity;

import com.wudgaby.platform.core.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 职位表
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysPosition对象", description="职位表")
public class SysPosition extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "职位名称")
    private String positionName;

    @ApiModelProperty(value = "职位编码")
    private String positionCode;

    @ApiModelProperty(value = "职位类型.1高层,2中层,3基层,10其他")
    private Integer positionType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态 0:正常, 1:停用")
    private Integer status;

    @ApiModelProperty(value = "乐观锁")
    private Integer version;

    //@ApiModelProperty(value = "逻辑删除 0:存在 1:删除")
    //private Boolean deleted;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "所属公司")
    private Long orgId;


}
