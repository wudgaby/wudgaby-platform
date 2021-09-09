package com.wudgaby.platform.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wudgaby.platform.permission.entity.BaseAction;
import com.wudgaby.platform.permission.vo.ActionForm;

/**
 * <p>
 * 系统资源-功能操作 服务类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
public interface BaseActionService extends IService<BaseAction> {

    Long addAction(ActionForm actionForm);

    void updateAction(ActionForm actionForm);

    void delAction(Long actionId);

    void delActionsByMenuId(Long menuId);
}
