package com.wudgaby.platform.permission.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 资源权限
 *
 * @author liuyadu
 */
@Data
public class AuthorityResource implements Serializable {
    /**
     * 访问路径
     */
    private String path;

    /**
     * 访问方法
     */
    private String method;

    /**
     * 权限标识
     */
    private String authority;

    /**
     * 权限ID
     */
    private Long authorityId;

    /**
     * 是否身份认证
     */
    private Integer isAuth;

    /**
     * 是否公开访问
     */
    private Integer isOpen;

    /**
     * 服务名称
     */
    private String serviceId;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 资源状态
     */
    private Integer status;
}
