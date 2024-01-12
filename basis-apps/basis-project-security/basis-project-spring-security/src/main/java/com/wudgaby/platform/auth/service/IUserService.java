package com.wudgaby.platform.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wudgaby.platform.auth.model.entity.User;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author WudGaby
 * @since 2020-04-17
 */
public interface IUserService extends IService<User> {
    User getByAccount(String account);
    User getByMail(String mail);
    User getByPhone(String phone);
}
