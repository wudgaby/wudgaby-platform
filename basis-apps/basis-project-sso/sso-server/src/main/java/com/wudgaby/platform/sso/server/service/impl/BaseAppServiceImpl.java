package com.wudgaby.platform.sso.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.sso.server.entity.BaseApp;
import com.wudgaby.platform.sso.server.form.AppForm;
import com.wudgaby.platform.sso.server.mapper.BaseAppMapper;
import com.wudgaby.platform.sso.server.service.BaseAppService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统应用-基础信息 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseAppServiceImpl extends ServiceImpl<BaseAppMapper, BaseApp> implements BaseAppService {

    @Override
    public Long addApp(AppForm appForm) {
        BaseApp baseApp = new BaseApp();
        baseApp.setAppCode(appForm.getAppCode());
        baseApp.setAppKey(RandomStringUtils.randomAlphabetic(10));
        baseApp.setAppSecret(RandomStringUtils.randomAlphabetic(30));
        baseApp.setAppName(appForm.getAppName());
        baseApp.setAppNameEn(appForm.getAppNameEn());
        baseApp.setAppIcon(appForm.getAppIcon());
        baseApp.setAppType(appForm.getAppType());
        baseApp.setAppDesc(appForm.getAppDesc());
        baseApp.setAppOs(appForm.getAppOs());
        baseApp.setWebsite(appForm.getWebsite());
        baseApp.setDeveloperId(appForm.getDeveloperId());
        this.save(baseApp);
        return baseApp.getAppId();
    }

    @Override
    public void updateApp(AppForm appForm) {
        BaseApp dbBaseApp = this.getById(appForm.getAppId());
        AssertUtil.notNull(dbBaseApp, "不存在该应用");

        BaseApp baseApp = new BaseApp();
        baseApp.setAppId(appForm.getAppId());
        baseApp.setAppName(appForm.getAppName());
        baseApp.setAppNameEn(appForm.getAppNameEn());
        baseApp.setAppIcon(appForm.getAppIcon());
        baseApp.setAppType(appForm.getAppType());
        baseApp.setAppDesc(appForm.getAppDesc());
        baseApp.setAppOs(appForm.getAppOs());
        baseApp.setWebsite(appForm.getWebsite());
        baseApp.setDeveloperId(appForm.getDeveloperId());
        this.updateById(baseApp);
    }

    @Override
    public void delApp(String appId) {
        BaseApp dbBaseApp = this.getById(appId);
        AssertUtil.notNull(dbBaseApp, "不存在该应用");
        AssertUtil.isTrue(dbBaseApp.getIsPersist() == 0, "该应用不允许删除");
        this.removeById(appId);
    }

    @Override
    public String resetAppSecret(Long appId) {
        BaseApp dbBaseApp = this.getById(appId);
        AssertUtil.notNull(dbBaseApp, "不存在该应用");

        BaseApp baseApp = new BaseApp();
        baseApp.setAppId(appId);
        baseApp.setAppSecret(RandomStringUtils.randomAlphabetic(30));
        this.updateById(baseApp);

        return baseApp.getAppSecret();
    }
}
