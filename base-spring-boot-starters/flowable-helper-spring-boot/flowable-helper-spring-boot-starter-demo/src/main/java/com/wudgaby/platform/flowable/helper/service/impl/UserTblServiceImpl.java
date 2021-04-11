package com.wudgaby.platform.flowable.helper.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.platform.flowable.helper.entity.UserTbl;
import com.wudgaby.platform.flowable.helper.mapper.UserTblMapper;
import com.wudgaby.platform.flowable.helper.service.UserTblService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zouyong
 * @since 2020-02-17
 */
@Service
@Slf4j
public class UserTblServiceImpl extends ServiceImpl<UserTblMapper, UserTbl> implements UserTblService {

}
