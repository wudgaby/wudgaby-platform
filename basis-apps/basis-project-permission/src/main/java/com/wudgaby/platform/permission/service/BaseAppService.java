package com.wudgaby.platform.permission.service;

import com.wudgaby.platform.permission.entity.BaseApp;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wudgaby.platform.permission.vo.AppForm;

/**
 * <p>
 * 系统应用-基础信息 服务类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
public interface BaseAppService extends IService<BaseApp> {

    Long addApp(AppForm appForm);

    void updateApp(AppForm appForm);

    void delApp(String appId);

    String resetAppSecret(Long appId);
}
