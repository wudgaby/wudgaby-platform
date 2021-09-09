package com.wudgaby.platform.sso.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wudgaby.platform.sso.server.entity.Resource;

import java.util.List;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author jimi
 * @since 2020-11-19
 */
public interface ResourceService extends IService<Resource> {
    List<Resource> getPermissionList(Long userId, String sysCode);
}
