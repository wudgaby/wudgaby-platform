package com.wudgaby.platform.core.support;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wudgaby.platform.core.constant.SystemConstant;
import com.wudgaby.platform.security.core.SecurityUtils;
import com.wudgaby.platform.security.core.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybatis-plus 的自动填充功能
 *
 * @ClassName CustomMetaObjectHandler
 * @Author zouyong
 * @Date 2019/7/3 0003 12:08
 **/
@Slf4j
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {
    private static final String CREATE_TIME_VALUE = "createTime";
    private static final String UPDATE_TIME_VALUE = "updateTime";

    private static final String CREATE_BY_VALUE = "createBy";
    private static final String UPDATE_BY_VALUE = "updateBy";

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, CREATE_TIME_VALUE, Date.class, new Date());

        String account = SecurityUtils.getCurrentUser().map(UserInfo::getAccount).orElse(SystemConstant.SYSTEM);
        this.strictInsertFill(metaObject, CREATE_BY_VALUE, String.class, account);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, UPDATE_TIME_VALUE, Date.class, new Date());

        String account = SecurityUtils.getCurrentUser().map(UserInfo::getAccount).orElse(SystemConstant.SYSTEM);
        this.strictUpdateFill(metaObject, UPDATE_BY_VALUE, String.class, account);
    }
}
