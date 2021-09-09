package com.wudgaby.platform.permission.service.impl;

import com.wudgaby.platform.permission.entity.BaseAuthorityAction;
import com.wudgaby.platform.permission.mapper.BaseAuthorityActionMapper;
import com.wudgaby.platform.permission.service.BaseAuthorityActionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统权限-功能操作关联表 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseAuthorityActionServiceImpl extends ServiceImpl<BaseAuthorityActionMapper, BaseAuthorityAction> implements BaseAuthorityActionService {

}
