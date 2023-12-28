package com.wudgaby.platform.sso.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wudgaby.platform.sso.server.entity.User;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/1 18:24
 * @Desc :
 */
public interface UserService extends IService<User> {
    User login(String account, String password);
}
