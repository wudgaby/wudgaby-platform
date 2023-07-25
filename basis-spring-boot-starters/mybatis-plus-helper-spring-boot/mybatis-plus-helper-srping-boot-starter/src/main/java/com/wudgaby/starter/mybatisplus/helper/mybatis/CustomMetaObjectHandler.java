package com.wudgaby.starter.mybatisplus.helper.mybatis;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wudgaby.platform.core.constant.SystemConstant;
import com.wudgaby.platform.security.core.SecurityUtils;
import com.wudgaby.platform.security.core.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;
import java.util.Optional;

/**
 * mybatis-plus 的自动填充功能
 *
 * @ClassName CustomMetaObjectHandler
 * @Author zouyong
 * @Date 2019/7/3 0003 12:08
 **/
@Slf4j
public class CustomMetaObjectHandler implements MetaObjectHandler {
    private static final String CREATE_TIME_VALUE = "createTime";
    private static final String UPDATE_TIME_VALUE = "updateTime";

    private static final String CREATE_BY_VALUE = "createBy";
    private static final String UPDATE_BY_VALUE = "updateBy";

    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = metaObject.getValue(CREATE_TIME_VALUE);
        if(ObjectUtil.isNull(createTime)) {
            this.strictInsertFill(metaObject, CREATE_TIME_VALUE, Date.class, new Date());
        }

        this.strictInsertFill(metaObject, CREATE_BY_VALUE, String.class, getLoginUsername());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, UPDATE_TIME_VALUE, Date.class, new Date());
        this.strictUpdateFill(metaObject, UPDATE_BY_VALUE, String.class, getLoginUsername());
    }

    /**
     * 获取de
     */
    private String getLoginUsername() {
        return Optional.ofNullable(SecurityUtils.getCurrentUser()).map(UserInfo::getUsername).orElse(SystemConstant.SYSTEM);
    }
}
