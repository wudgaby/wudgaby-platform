package com.wudgaby.sample.service.impl;

import com.wudgaby.starter.tenant.service.TenantFrameworkService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/17 0017 10:52
 * @desc :
 */
@Service
public class TenantFrameworkServiceImpl implements TenantFrameworkService {
    @Override
    public List<Long> getTenantIds() {
        return null;
    }

    @Override
    public void validTenant(Long id) {
        //是否存在,是否禁用等.
    }
}
