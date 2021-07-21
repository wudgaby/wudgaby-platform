package com.wudgaby.platform.flowable.helper.service;

import java.util.List;

/**
 * @ClassName : EmployeeService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/20 17:47
 * @Desc :   
 */
public interface EmployeeService {
    /**
     * 获取直接领导用户id
     * @param userId
     * @return
     */
    String getDirector(String userId);

    /**
     * 获取部门领导用户id
     * @param userId
     * @return
     */
    String getDeptDirector(String userId);

    /**
     * 获取指定用户的几级领导人
     * 该用户普通员工时
     * 1级领导 = 该用户当前部门的领导
     * 2级领导 = 该用户上级部门的领导
     * 3级领导 = 该用户上上级部门的领导
     *
     * 该用户部门领导时
     * 1级领导 = 该用户上级部门的领导
     * 2级领导 = 该用户上上级部门的领导
     * 3级领导 = 该用户上上上级部门的领导
     *
     * @param userId
     * @param level 1开始
     * @return
     */
    String getDirectorByLevel(String userId, int level);

    /**
     * 获取指定角色的用户id
     * @param roleId
     * @return
     */
    List<String> getEmployeesByRoleId(String roleId);

    /**
     * 获取指定角色的用户id
     * @param roleName
     * @return
     */
    List<String> getEmployeesByRoleName(String roleName);

    /**
     * 获取指定角色名称的角色id
     * @param roleName
     * @return
     */
    String getRoleIdByRoleName(String roleName);

    /**
     * 获取指定角色名称的角色id
     * @param roleNames
     * @return
     */
    List<String> getRoleIdsByRoleNames(String... roleNames);

    /**
     * 获取指定用户名账号id
     * @param userName
     * @return
     */
    List<String> getEmployeesByUsername(String userName);

    /**
     * 获取指定用户名账号id
     * @param userNames
     * @return
     */
    List<String> getEmployeesByUsernames(String... userNames);

    /**
     * 模糊查询
     * @param userName
     * @return
     */
    List<String> getEmployeesLikeUsername(String userName);

    /**
     * 获取指定账号id
     * @param account
     * @return
     */
    String getEmployeeByAccount(String account);

    /**
     * 获取指定账号id
     * @param accounts
     * @return
     */
    List<String> getEmployeesByAccounts(String... accounts);

    /**
     * 获取指定账号名称
     * @param userId
     * @return
     */
    String getEmployeeById(String userId);
}
