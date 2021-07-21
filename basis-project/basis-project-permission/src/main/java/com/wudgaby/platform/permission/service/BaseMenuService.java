package com.wudgaby.platform.permission.service;

import com.wudgaby.platform.permission.entity.BaseMenu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统资源-菜单信息 服务类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-17
 */
public interface BaseMenuService extends IService<BaseMenu> {

    Long addMenu(BaseMenu baseMenu);

    void updateMenu(BaseMenu baseMenu);

    void delMenu(Long menuId);
}
