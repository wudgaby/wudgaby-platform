package com.wudgaby.starter.datascope;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/12/31 1:20
 * @desc :
 */
public interface DataScopeService {
    /**
     * 获取角色自定义权限
     *
     * @param roleId 角色id
     * @return 部门id组
     */
    String getRoleCustom(Long roleId);

    /**
     * 获取部门及以下权限
     *
     * @param deptId 部门id
     * @return 部门id组
     */
    String getDeptAndChild(Long deptId);
}
