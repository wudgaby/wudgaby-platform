package com.wudgaby.starter.tenant.db;

import com.wudgaby.platform.core.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/12/30 1:36
 * @desc :
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantEntity extends BaseEntity {
    @ApiModelProperty("租户id")
    private Serializable tenantId;
}
