package com.wudgaby.platform.permission.service;

import com.wudgaby.platform.permission.entity.BaseUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统用户-管理员信息 服务类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-17
 */
public interface BaseUserService extends IService<BaseUser> {

    Long addUser(BaseUser baseUser);

    void updateUser(BaseUser baseUser);
}
