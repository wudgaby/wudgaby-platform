package com.wudgaby.platform.flowable.helper.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wudgaby.platform.flowable.helper.enums.DataStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author zouyong
 * @since 2020-02-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DeptTbl对象", description="部门表")
@TableName("oa_dept")
public class DeptTbl implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "部门ID")
    @TableId(value = "dept_id", type = IdType.AUTO)
    private Integer deptId;

    @ApiModelProperty(value = "父级部门ID")
    private Integer parentId;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "部门描述")
    private String deptDesc;

    @ApiModelProperty(value = "部门负责人ID")
    private String deptLeader;

    @ApiModelProperty(value = "0:正常  11：屏蔽")
    private DataStateEnum deptState;

    @ApiModelProperty(value = "部门深度")
    private String deptPath;

    @ApiModelProperty(value = "是否是父部门")
    @TableField("is_parent")
    private Boolean parent;

    @ApiModelProperty(value = "添加该部门用户id")
    private String addUserId;

    @ApiModelProperty(value = "添加时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "添加人id")
    private transient String addUserName;

}
