package com.wudgaby.starter.tenant;

import com.wudgaby.platform.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/12/30 1:36
 * @desc :
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantEntity extends BaseEntity {
    /**
     * 租户编号
     */
    private String tenantId;
}
