package com.wudgaby.platform.permission.mapper;

import com.wudgaby.platform.permission.dto.AuthorityMenu;
import com.wudgaby.platform.permission.dto.OpenAuthority;
import com.wudgaby.platform.permission.entity.BaseAuthorityRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统权限-角色关联 Mapper 接口
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
public interface BaseAuthorityRoleMapper extends BaseMapper<BaseAuthorityRole> {
    /**
     * 获取角色已授权权限
     *
     * @param roleId
     * @return
     */
    List<OpenAuthority> selectAuthorityByRole(@Param("roleId") Long roleId);

    /**
     * 获取角色菜单权限
     *
     * @param roleId
     * @return
     */
    List<AuthorityMenu> selectAuthorityMenuByRole(@Param("roleId") Long roleId);
}
