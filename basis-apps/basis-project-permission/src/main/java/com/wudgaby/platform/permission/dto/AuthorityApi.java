package com.wudgaby.platform.permission.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wudgaby.platform.permission.entity.BaseApi;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API权限
 * @author liuyadu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorityApi extends BaseApi {
    /**
     * 权限ID
     */
    private Long authorityId;

    /**
     * 权限标识
     */
    private String authority;

    /**
     * 前缀
     */
    private String prefix;
}
