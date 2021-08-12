package com.wudgaby.platform.permission.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.permission.consts.AuthorityConst;
import com.wudgaby.platform.permission.dto.AuthorityApi;
import com.wudgaby.platform.permission.dto.AuthorityMenu;
import com.wudgaby.platform.permission.dto.AuthorityResource;
import com.wudgaby.platform.permission.dto.OpenAuthority;
import com.wudgaby.platform.permission.entity.*;
import com.wudgaby.platform.permission.enums.ResourceType;
import com.wudgaby.platform.permission.mapper.*;
import com.wudgaby.platform.permission.service.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 系统权限-菜单权限、操作权限、API权限 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class BaseAuthorityServiceImpl extends ServiceImpl<BaseAuthorityMapper, BaseAuthority> implements BaseAuthorityService {
    private final BaseAuthorityActionService baseAuthorityActionService;
    private final BaseAuthorityAppService baseAuthorityAppService;
    private final BaseAuthorityRoleService baseAuthorityRoleService;
    private final BaseAuthorityUserService baseAuthorityUserService;

    private final BaseRoleService baseRoleService;
    @Autowired
    private BaseMenuService baseMenuService;
    @Autowired
    private BaseActionService baseActionService;
    @Autowired
    private BaseApiService baseApiService;
    private final BaseUserService baseUserService;
    private final BaseAppService baseAppService;

    private final BaseAuthorityAppMapper baseAuthorityAppMapper;
    private final BaseAuthorityRoleMapper baseAuthorityRoleMapper;
    private final BaseAuthorityUserMapper baseAuthorityUserMapper;
    private final BaseAuthorityActionMapper baseAuthorityActionMapper;

    @Override
    public List<AuthorityResource> findAuthorityResource() {
        // 已授权资源权限, 包含menu, action, api
        return this.getBaseMapper().selectAllAuthorityResource();
    }

    @Override
    public List<AuthorityMenu> findAuthorityMenu(Integer status) {
        return this.getBaseMapper().selectAuthorityMenu(status);
    }

    @Override
    public List<AuthorityApi> findAuthorityApi(String serviceId) {
        return this.getBaseMapper().selectAuthorityApi(1, serviceId);
    }

    @Override
    public List<BaseAuthorityAction> findAuthorityAction(Long actionId) {
        return baseAuthorityActionService.list(Wrappers.<BaseAuthorityAction>lambdaQuery().eq(BaseAuthorityAction::getActionId, actionId));
    }

    @Override
    public BaseAuthority saveOrUpdateAuthority(Long resourceId, ResourceType resourceType) {
        BaseAuthority baseAuthority = getAuthority(resourceId, resourceType);
        String authority = null;
        if (baseAuthority == null) {
            baseAuthority = new BaseAuthority();
        }
        if (ResourceType.MENU == resourceType) {
            BaseMenu menu = baseMenuService.getById(resourceId);
            authority = AuthorityConst.AUTHORITY_PREFIX_MENU + menu.getMenuCode();
            baseAuthority.setMenuId(resourceId);
            baseAuthority.setStatus(menu.getStatus());
        }
        if (ResourceType.ACTION.equals(resourceType)) {
            BaseAction operation = baseActionService.getById(resourceId);
            authority = AuthorityConst.AUTHORITY_PREFIX_ACTION + operation.getActionCode();
            baseAuthority.setActionId(resourceId);
            baseAuthority.setStatus(operation.getStatus());
        }
        if (ResourceType.API.equals(resourceType)) {
            BaseApi api = baseApiService.getById(resourceId);
            authority = AuthorityConst.AUTHORITY_PREFIX_API + api.getApiCode();
            baseAuthority.setApiId(resourceId);
            baseAuthority.setStatus(api.getStatus());
        }
        if (authority == null) {
            return null;
        }
        // 设置权限标识
        baseAuthority.setAuthority(authority);
        this.saveOrUpdate(baseAuthority);
        return baseAuthority;
    }

    @Override
    public BaseAuthority getAuthority(Long resourceId, ResourceType resourceType) {
        switch (resourceType){
            case MENU:
                return this.getOne(Wrappers.<BaseAuthority>lambdaQuery().eq(BaseAuthority::getMenuId, resourceId));
            case ACTION:
                return this.getOne(Wrappers.<BaseAuthority>lambdaQuery().eq(BaseAuthority::getActionId, resourceId));
            case API:
                return this.getOne(Wrappers.<BaseAuthority>lambdaQuery().eq(BaseAuthority::getApiId, resourceId));
            default:
                break;
        }
        return null;
    }

    @Override
    public void removeAuthority(Long resourceId, ResourceType resourceType) {
        switch (resourceType){
            case MENU:
                this.remove(Wrappers.<BaseAuthority>lambdaQuery().eq(BaseAuthority::getMenuId, resourceId));
                break;
            case ACTION:
                this.remove(Wrappers.<BaseAuthority>lambdaQuery().eq(BaseAuthority::getActionId, resourceId));
                break;
            case API:
                this.remove(Wrappers.<BaseAuthority>lambdaQuery().eq(BaseAuthority::getApiId, resourceId));
                break;
            default:
                //nothing
                break;
        }
    }

    @Override
    public void removeAuthorityApp(String appId) {
        baseAuthorityAppService.remove(Wrappers.<BaseAuthorityApp>lambdaQuery().eq(BaseAuthorityApp::getAppId, appId));
    }

    @Override
    public void removeAuthorityAction(Long actionId) {
        this.remove(Wrappers.<BaseAuthority>lambdaQuery().eq(BaseAuthority::getActionId, actionId));
    }

    @Override
    public Boolean isGranted(Long resourceId, ResourceType resourceType) {
        return null;
    }

    @Override
    public void addAuthorityRole(Long roleId, Date expireTime, List<Long> authorityIds) {
        BaseRole baseRole = baseRoleService.getById(roleId);
        AssertUtil.notNull(baseRole, "该角色不存在.");

        //先清空
        baseAuthorityRoleService.remove(Wrappers.<BaseAuthorityRole>lambdaQuery().eq(BaseAuthorityRole::getRoleId, roleId));
        if(CollectionUtils.isEmpty(authorityIds)) {
            return;
        }

        List<BaseAuthorityRole> baseAuthorityRoleList = Lists.newArrayList();
        authorityIds.forEach(authId -> {
            BaseAuthorityRole baseAuthorityRole = new BaseAuthorityRole();
            baseAuthorityRole.setAuthorityId(authId);
            baseAuthorityRole.setRoleId(roleId);
            baseAuthorityRole.setExpireTime(expireTime);
            baseAuthorityRoleList.add(baseAuthorityRole);
        });
        baseAuthorityRoleService.saveBatch(baseAuthorityRoleList);
    }

    @Override
    public void addAuthorityUser(Long userId, Date expireTime, List<Long> authorityIds) {
        BaseUser baseUser = baseUserService.getById(userId);
        AssertUtil.notNull(baseUser, "该用户不存在");

        if(baseUser.getUserName().equals(AuthorityConst.ADMIN)){
            throw new BusinessException("超级管理员无需授权");
        }

        //先清空
        baseAuthorityUserService.remove(Wrappers.<BaseAuthorityUser>lambdaQuery().eq(BaseAuthorityUser::getUserId, userId));
        if (CollectionUtils.isEmpty(authorityIds)) {
            return;
        }

        List<BaseAuthorityUser> baseAuthorityUserList = Lists.newArrayList();
        authorityIds.stream().forEach(authId -> {
            BaseAuthorityUser baseAuthorityUser = new BaseAuthorityUser();
            baseAuthorityUser.setAuthorityId(authId);
            baseAuthorityUser.setUserId(userId);
            baseAuthorityUser.setExpireTime(expireTime);
            baseAuthorityUserList.add(baseAuthorityUser);
        });
        baseAuthorityUserService.saveBatch(baseAuthorityUserList);
    }

    @Override
    public void addAuthorityApp(String appId, Date expireTime, List<Long> authorityIds) {
        BaseApp baseApp = baseAppService.getById(appId);
        AssertUtil.notNull(baseApp, "该APP不存在");

        //清空
        baseAuthorityAppService.remove(Wrappers.<BaseAuthorityApp>lambdaQuery().eq(BaseAuthorityApp::getAppId, appId));

        if(CollectionUtils.isEmpty(authorityIds)) {
            return;
        }

        List<BaseAuthorityApp> baseAuthorityAppList = Lists.newArrayList();
        authorityIds.forEach(authId -> {
            BaseAuthorityApp baseAuthorityApp = new BaseAuthorityApp();
            baseAuthorityApp.setAuthorityId(authId);
            baseAuthorityApp.setAppId(appId);
            baseAuthorityApp.setExpireTime(expireTime);
            baseAuthorityAppList.add(baseAuthorityApp);
        });

        baseAuthorityAppService.saveBatch(baseAuthorityAppList);
    }

    @Override
    public void addAuthorityAction(Long actionId, List<Long> authorityIds) {
        //先清空
        baseAuthorityActionService.remove(Wrappers.<BaseAuthorityAction>lambdaQuery().eq(BaseAuthorityAction::getActionId, actionId));

        if(CollectionUtils.isEmpty(authorityIds)) {
            return;
        }

        List<BaseAuthorityAction> baseAuthorityActionList = Lists.newArrayList();
        authorityIds.forEach(authId -> {
            BaseAuthorityAction baseAuthorityAction = new BaseAuthorityAction();
            baseAuthorityAction.setAuthorityId(authId);
            baseAuthorityAction.setActionId(actionId);
            baseAuthorityActionList.add(baseAuthorityAction);
        });
        baseAuthorityActionService.saveBatch(baseAuthorityActionList);
    }

    @Override
    public List<OpenAuthority> findAuthorityByApp(String appId) {
        return baseAuthorityAppMapper.selectAuthorityByApp(appId);
    }

    @Override
    public List<OpenAuthority> findAuthorityByRole(Long roleId) {
        return baseAuthorityRoleMapper.selectAuthorityByRole(roleId);
    }

    @Override
    public List<OpenAuthority> findAuthorityByUser(Long userId, Boolean isAdmin) {
        if(isAdmin) {
            // 超级管理员返回所有
            return findAuthorityByType("1");
        }
        List<OpenAuthority> authorities = Lists.newArrayList();

        //该用户所有的角色权限
        List<BaseRole> baseRoleList = baseRoleService.getUserRoles(userId);
        baseRoleList.forEach(role -> {
            List<OpenAuthority> roleGrantedAuthorityList = findAuthorityByRole(role.getRoleId());
            authorities.addAll(roleGrantedAuthorityList);
        });

        //该用户特殊权限
        List<OpenAuthority> userGrantedAuthorityList = baseAuthorityUserMapper.selectAuthorityByUser(userId);
        authorities.addAll(userGrantedAuthorityList);

        //去重
        HashSet hashSet = Sets.newHashSet(authorities);
        authorities.clear();
        authorities.addAll(hashSet);
        return authorities;
    }

    @Override
    public List<AuthorityMenu> findAuthorityMenuByUser(Long userId, Boolean isAdmin) {
        if(isAdmin) {
            // 超级管理员返回所有
            return findAuthorityMenu(null);
        }
        List<AuthorityMenu> authorityMenuList = Lists.newArrayList();

        //该用户所有的角色权限
        List<BaseRole> baseRoleList = baseRoleService.getUserRoles(userId);
        baseRoleList.forEach(role -> {
            List<AuthorityMenu> roleAuthorityMenuList = baseAuthorityRoleMapper.selectAuthorityMenuByRole(role.getRoleId());
            authorityMenuList.addAll(roleAuthorityMenuList);
        });

        //该用户特殊权限
        List<AuthorityMenu> userAuthorityMenuList = baseAuthorityUserMapper.selectAuthorityMenuByUser(userId);
        authorityMenuList.addAll(userAuthorityMenuList);

        //去重
        HashSet hashSet = Sets.newHashSet(authorityMenuList);
        authorityMenuList.clear();
        authorityMenuList.addAll(hashSet);
        //根据优先级从小到大排序
        authorityMenuList.sort((h1, h2) -> h1.getPriority().compareTo(h2.getPriority()));
        return authorityMenuList;
    }

    @Override
    public List<OpenAuthority> findAuthorityByType(String type) {
        return this.getBaseMapper().selectAuthorityAll(1, type);
    }

    @Override
    public Boolean isGrantedByRoleIds(String authorityId, List<Long> roleIds) {
        return null;
    }

    /**
     * 清理无效api数据
     * @param serviceId
     * @param codes
     */
    @Override
    public void clearInvalidApi(String serviceId, Collection<String> codes) {
        if (StringUtils.isBlank(serviceId)) {
            return;
        }
        List<String> invalidApiIds = baseApiService.listObjs(new QueryWrapper<BaseApi>()
                .select("api_id")
                .eq(StringUtils.isNotBlank(serviceId),"service_id", serviceId)
                .notIn(CollectionUtils.isNotEmpty(codes),"api_code", codes), e -> e.toString());

        if (CollectionUtils.isNotEmpty(invalidApiIds)) {
            // 防止删除默认api
            invalidApiIds.remove("1");
            invalidApiIds.remove("2");
            // 获取无效的权限
            if (invalidApiIds.isEmpty()) {
                return;
            }
            List<String> invalidAuthorityIds = listObjs(new QueryWrapper<BaseAuthority>()
                    .select("authority_id")
                    .in("api_id", invalidApiIds), e -> e.toString());

            if (CollectionUtils.isNotEmpty(invalidAuthorityIds)) {
                // 移除关联数据
                baseAuthorityAppMapper.delete(new QueryWrapper<BaseAuthorityApp>().in("authority_id", invalidAuthorityIds));
                baseAuthorityActionMapper.delete(new QueryWrapper<BaseAuthorityAction>().in("authority_id", invalidAuthorityIds));
                baseAuthorityRoleMapper.delete(new QueryWrapper<BaseAuthorityRole>().in("authority_id", invalidAuthorityIds));
                baseAuthorityUserMapper.delete(new QueryWrapper<BaseAuthorityUser>().in("authority_id", invalidAuthorityIds));
                // 移除权限数据
                this.removeByIds(invalidAuthorityIds);
                // 移除接口资源
                baseApiService.removeByIds(invalidApiIds);
            }
        }
    }
}
