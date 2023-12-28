package com.wudgaby.platform.permission.consts;

import lombok.experimental.UtilityClass;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/7/17 15:03
 * @Desc :
 */
@UtilityClass
public class AuthorityConst {
    public final static String ADMIN = "admin";

    public final static String AUTHORITY_PREFIX_MENU = "MENU_";
    public final static String AUTHORITY_PREFIX_ROLE = "ROLE_";
    public final static String AUTHORITY_PREFIX_API = "API_";
    public final static String AUTHORITY_PREFIX_ACTION = "ACTION_";

    /**
     * 默认接口分类
     */
    public final static String DEFAULT_API_CATEGORY = "default";

    /**
     * 状态:0-无效 1-有效
     */
    public final static int ENABLED = 1;
    public final static int DISABLED = 0;
}
