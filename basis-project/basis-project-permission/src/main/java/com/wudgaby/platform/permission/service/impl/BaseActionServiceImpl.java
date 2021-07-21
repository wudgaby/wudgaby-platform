package com.wudgaby.platform.permission.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.permission.entity.BaseAction;
import com.wudgaby.platform.permission.entity.BaseApi;
import com.wudgaby.platform.permission.entity.BaseAuthorityAction;
import com.wudgaby.platform.permission.enums.ResourceType;
import com.wudgaby.platform.permission.mapper.BaseActionMapper;
import com.wudgaby.platform.permission.service.BaseActionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.platform.permission.service.BaseAuthorityActionService;
import com.wudgaby.platform.permission.service.BaseAuthorityService;
import com.wudgaby.platform.permission.vo.ActionForm;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统资源-功能操作 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
@Service
@RequiredArgsConstructor
public class BaseActionServiceImpl extends ServiceImpl<BaseActionMapper, BaseAction> implements BaseActionService {
    @Autowired
    private BaseAuthorityService baseAuthorityService;
    private final BaseAuthorityActionService baseAuthorityActionService;

    @Override
    public Long addAction(ActionForm actionForm) {
        boolean exist = this.count(Wrappers.<BaseAction>lambdaQuery().eq(BaseAction::getActionCode, actionForm.getActionCode())) > 0;
        AssertUtil.isFalse(exist, "资源编码已存在.请换一个");

        BaseAction baseAction = new BaseAction();
        baseAction.setActionCode(actionForm.getActionCode());
        baseAction.setActionName(actionForm.getActionName());
        baseAction.setActionDesc(actionForm.getActionDesc());
        baseAction.setMenuId(actionForm.getMenuId());
        baseAction.setPriority(actionForm.getPriority());
        baseAction.setServiceId(actionForm.getServiceId());

        this.save(baseAction);
        //新增/修改权限
        baseAuthorityService.saveOrUpdateAuthority(baseAction.getActionId(), ResourceType.ACTION);
        return baseAction.getActionId();
    }

    @Override
    public void updateAction(ActionForm actionForm) {
        BaseAction dbBaseAction = this.getById(actionForm.getActionId());
        AssertUtil.notNull(dbBaseAction, "不存在该功能按钮资源");

        boolean exist = this.count(Wrappers.<BaseAction>lambdaQuery()
                .eq(BaseAction::getActionId, actionForm.getActionId())
                .ne(BaseAction::getActionCode, actionForm.getActionCode())
        ) > 0;
        AssertUtil.isFalse(exist, "资源编码已存在.");

        BaseAction baseAction = new BaseAction();
        baseAction.setActionId(actionForm.getActionId());
        baseAction.setActionCode(actionForm.getActionCode());
        baseAction.setActionName(actionForm.getActionName());
        baseAction.setActionDesc(actionForm.getActionDesc());
        baseAction.setMenuId(actionForm.getMenuId());
        baseAction.setPriority(actionForm.getPriority());
        baseAction.setServiceId(actionForm.getServiceId());

        this.updateById(baseAction);
        //新增/修改权限
        baseAuthorityService.saveOrUpdateAuthority(baseAction.getActionId(), ResourceType.ACTION);
    }

    @Override
    public void delAction(Long actionId) {
        BaseAction dbBaseAction = this.getById(actionId);
        AssertUtil.notNull(dbBaseAction, "不存在该功能按钮资源");
        AssertUtil.isTrue(dbBaseAction.getIsPersist() == 0, "保留数据,该项不允许删除");

        //删除功能按钮权限
        baseAuthorityActionService.remove(Wrappers.<BaseAuthorityAction>lambdaQuery().eq(BaseAuthorityAction::getActionId, actionId));
        //删除权限
        baseAuthorityService.removeAuthorityAction(actionId);
        this.removeById(actionId);
    }

    @Override
    public void delActionsByMenuId(Long menuId) {
        List<BaseAction> baseActionList = this.list(Wrappers.<BaseAction>lambdaQuery().eq(BaseAction::getMenuId, menuId));
        baseActionList.forEach(baseAction -> {
            delAction(baseAction.getActionId());
        });
    }
}
