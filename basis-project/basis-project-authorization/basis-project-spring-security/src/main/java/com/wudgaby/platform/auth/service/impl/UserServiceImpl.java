package com.wudgaby.platform.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.platform.auth.mapper.UserMapper;
import com.wudgaby.platform.auth.model.entity.User;
import com.wudgaby.platform.auth.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2020-04-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    public User getByAccount(String account){
        return this.baseMapper.findByAccount(account);
    }

    @Override
    public User getByMail(String mail) {
        return this.baseMapper.findByEmail(mail);
    }

    @Override
    public User getByPhone(String phone) {
        return this.baseMapper.findByPhone(phone);
    }
}
