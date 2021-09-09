package com.wudgaby.platform.permission.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.permission.consts.AuthorityConst;
import com.wudgaby.platform.permission.entity.BaseApi;
import com.wudgaby.platform.permission.enums.ResourceType;
import com.wudgaby.platform.permission.mapper.BaseApiMapper;
import com.wudgaby.platform.permission.service.BaseApiService;
import com.wudgaby.platform.permission.service.BaseAuthorityService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统资源-API接口 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class BaseApiServiceImpl extends ServiceImpl<BaseApiMapper, BaseApi> implements BaseApiService {
    private final BaseAuthorityService baseAuthorityService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addApi(BaseApi baseApi) {
        boolean isExist = this.count(Wrappers.<BaseApi>lambdaQuery().eq(BaseApi::getApiCode, baseApi.getApiCode())) > 0;
        AssertUtil.isFalse(isExist, "接口编码已存在.");

        if (StringUtils.isBlank(baseApi.getApiCategory())) {
            baseApi.setApiCategory(AuthorityConst.DEFAULT_API_CATEGORY);
        }
        if (baseApi.getIsAuth() == null) {
            baseApi.setIsAuth(0);
        }
        if (baseApi.getIsOpen() == null) {
            baseApi.setIsOpen(0);
        }
        if (baseApi.getStatus() == null) {
            baseApi.setStatus(AuthorityConst.ENABLED);
        }

        this.save(baseApi);
        //同步权限表
        baseAuthorityService.saveOrUpdateAuthority(baseApi.getApiId(), ResourceType.API);
        return baseApi.getApiId();
    }

    @Override
    public void updateApi(BaseApi baseApi) {
        BaseApi dbBaseApi = this.getById(baseApi.getApiId());
        AssertUtil.notNull(dbBaseApi, "不存在该接口资源");
        boolean isExist = dbBaseApi.getApiCode().equals(baseApi.getApiCode()) && !dbBaseApi.getApiId().equals(dbBaseApi.getApiId());
        AssertUtil.isFalse(isExist, "该接口编码已存在.");

        if (StringUtils.isBlank(baseApi.getApiCategory())) {
            baseApi.setApiCategory(AuthorityConst.DEFAULT_API_CATEGORY);
        }

        this.updateById(baseApi);
        //同步权限表
        baseAuthorityService.saveOrUpdateAuthority(baseApi.getApiId(), ResourceType.API);
    }

    @Override
    public void delApi(Long apiId) {
        BaseApi dbBaseApi = this.getById(apiId);
        AssertUtil.notNull(dbBaseApi, "不存在该接口资源");
        AssertUtil.isTrue(dbBaseApi.getIsPersist() == 0, "该接口资源不允许删除");

        //删除权限
        baseAuthorityService.removeAuthority(apiId, ResourceType.API);
        this.removeById(apiId);
    }
}
