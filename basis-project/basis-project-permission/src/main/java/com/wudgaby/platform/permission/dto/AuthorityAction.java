package com.wudgaby.platform.permission.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wudgaby.platform.permission.entity.BaseAction;

/**
 * 功能权限
 * @author liuyadu
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorityAction extends BaseAction {
    /**
     * 权限ID
     */
    private Long authorityId;

    /**
     * 权限标识
     */
    private String authority;

    /**
     * 是否需要安全认证
     */
    private Boolean isAuth = true;

}
