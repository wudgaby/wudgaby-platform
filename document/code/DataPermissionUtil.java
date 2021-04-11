package com.ruoyi.assetspackage.util;

import com.ruoyi.framework.aspectj.DataScopeAspect;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysRoleDept;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 数据权限util
 * @author: liurunkai
 * @Date: 2020/1/11 16:01
 */
@Slf4j
@Component
public class PackageDataPermissionUtil {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;

    /**
     * 判断是否有查看所有数据的权限
     *
     * @param sysUser
     * @return
     */
    public boolean findAllDataPermissionByUser(SysUser sysUser) {
        // 获取用户角色
        List<SysRole> sysRoles = this.sysRoleMapper.selectRolesByUserId(sysUser.getUserId());
        // 根据用户角色获取数据权限
        Set<String> dataScopeSet = sysRoles.stream()
                .map(sysRole -> sysRole.getDataScope())
                .collect(Collectors.toSet());
        if (dataScopeSet.contains(DataScopeAspect.DATA_SCOPE_ALL)) {
            return true;
        }
        return false;
    }

    /**
     * 获取用户有查看哪些权限的部门
     *
     * @param sysUser
     * @return
     */
    public Set<Long> findDeptDataPermissionByUser(SysUser sysUser) {
        // 获取用户角色
        List<SysRole> sysRoles = this.sysRoleMapper.selectRolesByUserId(sysUser.getUserId());
        // 根据用户角色获取数据权限
        Set<String> dataScopeSet = sysRoles.stream()
                .map(sysRole -> sysRole.getDataScope())
                .collect(Collectors.toSet());
        Set<Long> deptSet = new HashSet<>();
        if (dataScopeSet.contains(DataScopeAspect.DATA_SCOPE_DEPT_AND_CHILD)) {
            //部门及以下数据权限
            deptSet.add(sysUser.getDeptId());
            getChildDept(deptSet, sysUser.getDeptId());
        } else if (dataScopeSet.contains(DataScopeAspect.DATA_SCOPE_DEPT)) {
            //部门数据权限
            deptSet.add(sysUser.getDeptId());
        }
        if (dataScopeSet.contains(DataScopeAspect.DATA_SCOPE_CUSTOM)) {
            //自定数据权限：根据角色id查询有权限查看的部门
            sysRoles.stream()
                    .forEach(sysRole -> {
                        List<SysRoleDept> roleDepts = this.sysDeptMapper.selectDeptIdByRoleId(sysRole.getRoleId());
                        roleDepts.stream().forEach(sysRoleDept -> deptSet.add(sysRoleDept.getDeptId()));
                    });
        }
        return deptSet;
    }

    /**
     * 判断是否有查看本人数据的权限
     *
     * @param sysUser
     * @return
     */
    public boolean findSelfDataPermissionByUser(SysUser sysUser) {
        // 获取用户角色
        List<SysRole> sysRoles = this.sysRoleMapper.selectRolesByUserId(sysUser.getUserId());
        // 根据用户角色获取数据权限
        Set<String> dataScopeSet = sysRoles.stream()
                .map(sysRole -> sysRole.getDataScope())
                .collect(Collectors.toSet());
        if (dataScopeSet.contains(DataScopeAspect.DATA_SCOPE_SELF)) {
            return true;
        }
        return false;
    }

    /**
     * 根据父部门id查询所有子部门
     *
     * @param deptSet
     * @param deptId
     */
    private void getChildDept(Set<Long> deptSet, Long deptId) {
        List<SysDept> sysDeptList = this.sysDeptMapper.selectDeptByParentId(deptId);
        sysDeptList.stream()
                .forEach(sysDept -> {
                    deptSet.add(sysDept.getDeptId());
                    getChildDept(deptSet, sysDept.getDeptId());
                });
    }

    /**
     * 查看拥有的部门权限
     *
     * @return
     */
    public Set<Long> selectDataPer() {
        Set<Long> deptIds = null;
        boolean allDataPermissionByUser = findAllDataPermissionByUser(ShiroUtils.getSysUser());
        if (!allDataPermissionByUser) {
            Set<Long> deptDataPermission = findDeptDataPermissionByUser(ShiroUtils.getSysUser());
            if (deptDataPermission != null && deptDataPermission.size() > 0) {
                deptIds = deptDataPermission;
            } else {
                boolean selfDataPermission = findSelfDataPermissionByUser(ShiroUtils.getSysUser());
                if (!selfDataPermission) {
                    log.error("没有查看数据的权限，请联系管理员授权");
                    throw new RuntimeException("没有查看数据的权限，请联系管理员授权");
                }
                deptIds = new HashSet<>();
                deptIds.add(ShiroUtils.getSysUser().getDeptId());
            }
        }
        return deptIds;
    }
}