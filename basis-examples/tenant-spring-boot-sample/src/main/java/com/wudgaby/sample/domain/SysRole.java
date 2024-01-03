package com.wudgaby.sample.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wudgaby.starter.tenant.TenantEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 角色
 * @TableName sys_role
 */
@TableName(value ="sys_role")
@Data
@Accessors(chain = true)
public class SysRole extends TenantEntity implements Serializable {
    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 描述
     */
    private String roleDesc;

    /**
     * 状态 0:正常, 1:停用
     */
    private Integer status;

    /**
     * 逻辑删除 0:存在 1:删除
     */
    private Integer deleted;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否系统级别 1:是,0否
     */
    private Integer isSys;
}