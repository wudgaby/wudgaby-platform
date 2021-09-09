package com.wudgaby.platform.permission.service;

import com.wudgaby.platform.permission.entity.BaseApi;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统资源-API接口 服务类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
public interface BaseApiService extends IService<BaseApi> {

    Long addApi(BaseApi baseApi);

    void updateApi(BaseApi baseApi);

    void delApi(Long apiId);
}
