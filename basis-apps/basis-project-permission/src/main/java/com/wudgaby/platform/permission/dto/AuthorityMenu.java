package com.wudgaby.platform.permission.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wudgaby.platform.permission.entity.BaseMenu;
import lombok.Data;

import java.util.List;

/**
 * 菜单权限
 * @author liuyadu
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorityMenu extends BaseMenu {
    /**
     * 权限ID
     */
    private Long authorityId;

    /**
     * 权限标识
     */
    private String authority;

    private List<AuthorityAction> actionList;
}
