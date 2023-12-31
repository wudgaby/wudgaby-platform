package com.wudgaby.starter.mybatisplus.helper.mybatis;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wudgaby.platform.core.entity.BaseEntity;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.security.core.SecurityUtils;
import com.wudgaby.platform.security.core.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;
import java.util.Optional;

/**
 * mybatis-plus 的自动填充功能
 *
 * @author zouyong
 * @date 2019/7/3 0003 12:08
 **/
@Slf4j
public class CustomMetaObjectHandler implements MetaObjectHandler {
    private static final String CREATE_TIME_VALUE = "createTime";
    private static final String UPDATE_TIME_VALUE = "updateTime";

    private static final String CREATE_BY_VALUE = "createBy";
    private static final String UPDATE_BY_VALUE = "updateBy";

    @Override
    public void insertFill(MetaObject metaObject) {
        if(ObjectUtil.isNull(metaObject) ){
            return;
        }

        try {
            if (metaObject.getOriginalObject() instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
                Date current = ObjectUtil.isNotNull(baseEntity.getCreateTime()) ? baseEntity.getCreateTime() : new Date();
                baseEntity.setCreateTime(current);
                baseEntity.setUpdateTime(current);

                Optional<UserInfo> loginUser = SecurityUtils.getOptionalUser();
                if (loginUser.isPresent()) {
                    String userId = ObjectUtil.isNotNull(baseEntity.getCreateBy()) ? baseEntity.getCreateBy() : String.valueOf(loginUser.get());
                    // 当前已登录 且 创建人为空 则填充
                    baseEntity.setCreateBy(userId);
                    // 当前已登录 且 更新人为空 则填充
                    baseEntity.setUpdateBy(userId);
                }
            } else {
                Object createTime = metaObject.getValue(CREATE_TIME_VALUE);
                Date current = ObjectUtil.isNotNull(createTime) ? (Date) createTime : new Date();
                this.strictInsertFill(metaObject, CREATE_TIME_VALUE, Date.class, current);
                this.strictInsertFill(metaObject, UPDATE_TIME_VALUE, Date.class, current);

                Optional<UserInfo> loginUser = SecurityUtils.getOptionalUser();
                if (loginUser.isPresent()) {
                    Object createBy = metaObject.getValue(CREATE_BY_VALUE);
                    String userId = ObjectUtil.isNotNull(createBy) ? (String) createBy : String.valueOf(loginUser.get());
                    this.strictInsertFill(metaObject, CREATE_BY_VALUE, String.class, userId);
                    this.strictInsertFill(metaObject, UPDATE_BY_VALUE, String.class, userId);
                }
            }
        } catch (Exception e) {
            throw new BusinessException("自动注入异常 => " + e.getMessage());
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if(ObjectUtil.isNull(metaObject) ){
            return;
        }

        try {
            if (metaObject.getOriginalObject() instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
                // 更新时间填充(不管为不为空)
                baseEntity.setUpdateTime(new Date());

                Optional<UserInfo> loginUser = SecurityUtils.getOptionalUser();
                if (loginUser.isPresent()) {
                    String userId = ObjectUtil.isNotNull(baseEntity.getUpdateBy()) ? baseEntity.getUpdateBy() : String.valueOf(loginUser.get());
                    // 当前已登录 且 更新人为空 则填充
                    baseEntity.setUpdateBy(userId);
                }
            }else{
                this.strictInsertFill(metaObject, UPDATE_TIME_VALUE, Date.class, new Date());

                Optional<UserInfo> loginUser = SecurityUtils.getOptionalUser();
                if (loginUser.isPresent()) {
                    Object updateBy = metaObject.getValue(UPDATE_BY_VALUE);
                    String userId = ObjectUtil.isNotNull(updateBy) ? (String) updateBy : String.valueOf(loginUser.get());
                    this.strictInsertFill(metaObject, UPDATE_BY_VALUE, String.class, userId);
                }
            }
        } catch (Exception e) {
            throw new BusinessException("自动注入异常 => " + e.getMessage());
        }
    }

}
