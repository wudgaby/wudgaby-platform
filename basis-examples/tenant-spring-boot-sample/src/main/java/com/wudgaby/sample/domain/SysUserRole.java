package com.wudgaby.sample.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wudgaby.platform.core.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户-角色关系表
 * @TableName sys_user_role
 */
@TableName(value ="sys_user_role")
@Data
public class SysUserRole extends BaseEntity implements Serializable {

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 用户id
     */
    private Long userId;
}