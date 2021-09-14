package com.wudgaby.platform.sso.server.service.impl;

import com.wudgaby.platform.sso.server.entity.UserApp;
import com.wudgaby.platform.sso.server.mapper.UserAppMapper;
import com.wudgaby.platform.sso.server.service.UserAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-应用 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2021-09-13
 */
@Service
public class UserAppServiceImpl extends ServiceImpl<UserAppMapper, UserApp> implements UserAppService {

}
