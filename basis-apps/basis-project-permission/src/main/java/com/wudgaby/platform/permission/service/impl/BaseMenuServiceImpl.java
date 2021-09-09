package com.wudgaby.platform.permission.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.permission.entity.BaseMenu;
import com.wudgaby.platform.permission.enums.ResourceType;
import com.wudgaby.platform.permission.mapper.BaseMenuMapper;
import com.wudgaby.platform.permission.service.BaseActionService;
import com.wudgaby.platform.permission.service.BaseAuthorityService;
import com.wudgaby.platform.permission.service.BaseMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统资源-菜单信息 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-17
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class BaseMenuServiceImpl extends ServiceImpl<BaseMenuMapper, BaseMenu> implements BaseMenuService {
    @Autowired
    private BaseActionService baseActionService;
    private final BaseAuthorityService baseAuthorityService;

    @Override
    public Long addMenu(BaseMenu baseMenu) {
        boolean isExist = this.count(Wrappers.<BaseMenu>lambdaQuery().eq(BaseMenu::getMenuCode, baseMenu.getMenuCode())) > 0;
        AssertUtil.isFalse(isExist, "该菜单编码已存在.");

        this.save(baseMenu);
        //同步权限表
        baseAuthorityService.saveOrUpdateAuthority(baseMenu.getMenuId(), ResourceType.MENU);
        return baseMenu.getMenuId();
    }

    @Override
    public void updateMenu(BaseMenu baseMenu) {
        BaseMenu dbBaseMenu = this.getById(baseMenu.getMenuId());
        AssertUtil.notNull(dbBaseMenu, "该菜单不存在");
        boolean isExist = dbBaseMenu.getMenuCode().equals(baseMenu.getMenuCode()) && !dbBaseMenu.getMenuId().equals(baseMenu.getMenuId());
        AssertUtil.isFalse(isExist, "该编码已存在");

        this.updateById(baseMenu);
        //同步权限表
        baseAuthorityService.saveOrUpdateAuthority(baseMenu.getMenuId(), ResourceType.MENU);
    }

    @Override
    public void delMenu(Long menuId) {
        BaseMenu dbBaseMenu = this.getById(menuId);
        AssertUtil.notNull(dbBaseMenu, "该菜单不存在");
        AssertUtil.isTrue(dbBaseMenu.getIsPersist() == 0, "该菜单不允许删除");

        //删除菜单下功能
        baseActionService.delActionsByMenuId(menuId);
        //删除权限
        baseAuthorityService.removeAuthority(menuId, ResourceType.MENU);
        this.delMenu(menuId);
    }
}
