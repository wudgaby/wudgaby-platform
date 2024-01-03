package com.wudgaby.sample.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wudgaby.platform.core.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 租户
 * @TableName sys_tenant
 */
@TableName(value ="sys_tenant")
@Data
@Accessors(chain = true)
public class SysTenant extends BaseEntity implements Serializable {
    /**
     * 状态 0:正常, 1:停用
     */
    private Integer status;

    /**
     * 逻辑删除 0:存在 1:删除
     */
    private Integer deleted;

    /**
     * 租户名
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

}