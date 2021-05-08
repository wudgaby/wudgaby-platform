package com.wudgaby.platform.sso.core.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 权限对象
 */
@Data
public class PermissionVo implements Serializable{
    private String code;
    private String type;
    private String uri;
    private String method;
    private String name;
    private String menu;
}
