package com.wudgaby.platform.flowable.helper.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.wudgaby.platform.flowable.helper.consts.SysConstant;
import com.wudgaby.platform.flowable.helper.entity.DeptTbl;
import com.wudgaby.platform.flowable.helper.entity.RoleTbl;
import com.wudgaby.platform.flowable.helper.entity.UserRole;
import com.wudgaby.platform.flowable.helper.entity.UserTbl;
import com.wudgaby.platform.flowable.helper.enums.DataStateEnum;
import com.wudgaby.platform.flowable.helper.mapper.DeptTblMapper;
import com.wudgaby.platform.flowable.helper.mapper.UserRoleMapper;
import com.wudgaby.platform.flowable.helper.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName : EmployeeServiceImpl
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/20 17:58
 * @Desc :
 */
@Slf4j
@Service("empService")
public class EmployeeServiceImpl implements EmployeeService {
    private static final int MAX_LEVEL = 50;
    @Autowired
    private UserTblService userTblService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private DeptTblService deptTblService;
    @Autowired
    private RoleTblService roleTblService;

    @Override
    public String getDirector(String userId) {
        String directorUserId = getDeptDirector(userId);
        DeptTblMapper mapper = (DeptTblMapper)deptTblService.getBaseMapper();
        boolean isDirector = directorUserId.equals(userId);

        //该用户本身就是领导时,获取上个部门领导
        if(isDirector){
            DeptTbl deptTbl = mapper.getDeptByUserId(userId);
            if(deptTbl == null || deptTbl.getParentId() == SysConstant.TREE_ROOT){
                return "";
            }
            DeptTbl parentDept = deptTblService.getById(deptTbl.getParentId());
            if(parentDept == null){
                return "";
            }
            return Optional.ofNullable(parentDept.getDeptLeader()).orElse("");
        }
        return directorUserId;
    }

    @Override
    public String getDeptDirector(String userId) {
        DeptTblMapper mapper = (DeptTblMapper)deptTblService.getBaseMapper();
        DeptTbl deptTbl = mapper.getDeptByUserId(userId);
        if(deptTbl == null){
            log.warn("该用户不属于任何部门. userId: {}", userId);
            return "";
        }
        return Optional.ofNullable(deptTbl.getDeptLeader()).orElse("");
    }

    /**
     * boolean isFast
     * @param userId
     * @param level 1开始
     * @return
     */
    @Override
    public String getDirectorByLevel(String userId, int level) {
        if(level <= 0 || level > MAX_LEVEL){
            log.warn("不支持的领导级别. level :{}", level);
            return "";
        }
        DeptTblMapper mapper = (DeptTblMapper)deptTblService.getBaseMapper();

        String directorUserId = getDeptDirector(userId);
        boolean isDirector = userId.equals(directorUserId);

        //当前用户的一级领导是自己 直接审批
        /*if(level == 1 && isFast && isDirector){
            return directorUserId;
        }*/

        DeptTbl deptTbl = mapper.getDeptByUserId(userId);
        if(deptTbl == null){
            log.warn("该用户不属于任何部门. userId: {}", userId);
            return "";
        }
        String deptPath = deptTbl.getDeptPath();

        List<Integer> pathList = Splitter.on(SysConstant.SYMBOL_DOT).omitEmptyStrings().trimResults().splitToList(deptPath)
                                            .stream().map(Integer::valueOf).collect(Collectors.toList());
        Collections.reverse(pathList);

        //本身是领导的话,取上级部门领导
        if(isDirector){
            ++level;
        }

        if(pathList.size() < level){
            return "";
        }

        Integer levelDeptId = pathList.get(level - 1);
        DeptTbl levelDept = deptTblService.getById(levelDeptId);
        if(levelDept == null){
            return "";
        }
        return Optional.ofNullable(levelDept.getDeptLeader()).orElse("");
    }

    @Override
    public List<String> getEmployeesByRoleId(String roleId) {
        List<UserRole> userRoleList = userRoleService.list(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId, roleId));
        if(CollectionUtils.isNotEmpty(userRoleList)){
            List<String> userIdList = userRoleList.stream().map(ur -> ur.getUserId()).collect(Collectors.toList());

            //只查正常用户
            List<UserTbl> userList = userTblService.list(Wrappers.<UserTbl>lambdaQuery().in(UserTbl::getUserId, userIdList)
                    .eq(UserTbl::getUserStatus, DataStateEnum.NORMAL)
                    .eq(UserTbl::getDeleted, false));

            if(CollectionUtils.isNotEmpty(userList)){
                return userList.stream().map(u -> u.getUserId()).collect(Collectors.toList());
            }

        }
        log.warn("该角色下无任何用户. roleId: {}", roleId);
        return Lists.newArrayList();
    }

    @Override
    public List<String> getEmployeesByRoleName(String roleName) {
        UserRoleMapper mapper = (UserRoleMapper)userRoleService.getBaseMapper();
        List<String> userIdList = mapper.findByRoleName(roleName);
        if(CollectionUtils.isNotEmpty(userIdList)){
            //只查正常用户
            List<UserTbl> userList = userTblService.list(Wrappers.<UserTbl>lambdaQuery().in(UserTbl::getUserId, userIdList)
                                                        .eq(UserTbl::getUserStatus, DataStateEnum.NORMAL)
                                                        .eq(UserTbl::getDeleted, false));
            if(CollectionUtils.isNotEmpty(userList)){
                return userList.stream().map(u -> u.getUserId()).collect(Collectors.toList());
            }
        }
        log.warn("该角色下无任何用户. roleName: {}", roleName);
        return Lists.newArrayList();
    }

    @Override
    public List<String> getEmployeesByUsername(String userName) {
        List<UserTbl> userList = userTblService.list(Wrappers.<UserTbl>lambdaQuery()
                .eq(UserTbl::getName, userName)
                .eq(UserTbl::getUserStatus, DataStateEnum.NORMAL)
                .eq(UserTbl::getDeleted, false)
                .orderByAsc(UserTbl::getCreateTime));
        if(CollectionUtils.isNotEmpty(userList)){
            return userList.stream().map(ur -> ur.getUserId()).collect(Collectors.toList());
        }
        log.warn("系统无该用户名. userName: {}", userName);
        return Lists.newArrayList();
    }

    @Override
    public List<String> getEmployeesLikeUsername(String userName) {
        List<UserTbl> userList = userTblService.list(Wrappers.<UserTbl>lambdaQuery()
                .like(UserTbl::getName, "%" + userName + "%")
                .eq(UserTbl::getUserStatus, DataStateEnum.NORMAL)
                .eq(UserTbl::getDeleted, false)
                .orderByAsc(UserTbl::getCreateTime));
        if(CollectionUtils.isNotEmpty(userList)){
            return userList.stream().map(ur -> ur.getUserId()).collect(Collectors.toList());
        }
        log.warn("系统无该用户名. userName: {}", userName);
        return Lists.newArrayList();
    }

    @Override
    public String getEmployeeByAccount(String account) {
        UserTbl user = userTblService.getOne(Wrappers.<UserTbl>lambdaQuery()
                .eq(UserTbl::getAccount, account)
                .eq(UserTbl::getUserStatus, DataStateEnum.NORMAL)
                .eq(UserTbl::getDeleted, false));
        if(user != null){
            return user.getUserId();
        }
        log.warn("系统无该账号. account: {}", account);
        return "";
    }

    @Override
    public String getEmployeeById(String userId) {
        UserTbl user = userTblService.getById(userId);
        if(user != null){
            return user.getAccount();
        }
        log.warn("系统无该账号. userId: {}", userId);
        return "";
    }

    @Override
    public String getRoleIdByRoleName(String roleName) {
        RoleTbl role = roleTblService.getOne(Wrappers.<RoleTbl>lambdaQuery().eq(RoleTbl::getRoleName, roleName));
        if(role != null){
            return role.getRoleId();
        }
        log.warn("系统无该角色. roleName: {}", roleName);
        return "";
    }

    @Override
    public List<String> getRoleIdsByRoleNames(String... roleNames) {
        List<RoleTbl> roleList = roleTblService.list(Wrappers.<RoleTbl>lambdaQuery().in(RoleTbl::getRoleName, Lists.newArrayList(roleNames)));
        if(CollectionUtils.isNotEmpty(roleList)){
            return roleList.stream().map(r -> r.getRoleId()).collect(Collectors.toList());
        }
        log.warn("系统无该角色. roleNames: {}", Arrays.asList(roleNames));
        return Lists.newArrayList();
    }

    @Override
    public List<String> getEmployeesByUsernames(String... userNames) {
        List<UserTbl> userList = userTblService.list(Wrappers.<UserTbl>lambdaQuery()
                .in(UserTbl::getName, Lists.newArrayList(userNames))
                .eq(UserTbl::getUserStatus, DataStateEnum.NORMAL)
                .eq(UserTbl::getDeleted, false));
        if(CollectionUtils.isNotEmpty(userList)){
            return userList.stream().map(u -> u.getUserId()).collect(Collectors.toList());
        }
        log.warn("系统无该用户. userNames: {}", Arrays.asList(userNames));
        return Lists.newArrayList();
    }

    @Override
    public List<String> getEmployeesByAccounts(String... accounts) {
        List<UserTbl> userList = userTblService.list(Wrappers.<UserTbl>lambdaQuery()
                .in(UserTbl::getAccount, Lists.newArrayList(accounts))
                .eq(UserTbl::getUserStatus, DataStateEnum.NORMAL)
                .eq(UserTbl::getDeleted, false));
        if(CollectionUtils.isNotEmpty(userList)){
            return userList.stream().map(u -> u.getUserId()).collect(Collectors.toList());
        }
        log.warn("系统无该账号. accounts: {}", Arrays.asList(accounts));
        return Lists.newArrayList();
    }
}
