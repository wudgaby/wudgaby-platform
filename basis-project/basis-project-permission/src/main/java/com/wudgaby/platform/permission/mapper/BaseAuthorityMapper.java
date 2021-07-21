package com.wudgaby.platform.permission.mapper;

import com.wudgaby.platform.permission.dto.*;
import com.wudgaby.platform.permission.entity.BaseAuthority;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统权限-菜单权限、操作权限、API权限 Mapper 接口
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
public interface BaseAuthorityMapper extends BaseMapper<BaseAuthority> {
    /**
     * 查询所有资源授权列表
     * @return
     */
    List<AuthorityResource> selectAllAuthorityResource();

    /**
     * 查询已授权权限列表
     * type = null 查询全部
     * type = 1 获取菜单和操作
     * type = 2 获取API
     * @param status
     * @param type
     * @return
     */
    List<OpenAuthority> selectAuthorityAll(@Param("status") int status, @Param("type")String type);

    /**
     * 获取菜单权限
     *
     * @param map
     * @return
     */
    List<AuthorityMenu> selectAuthorityMenu(@Param("status")Integer status);

    /**
     * 获取操作权限
     *
     * @param map
     * @return
     */
    List<AuthorityAction> selectAuthorityAction(@Param("status")Integer status, @Param("menuId")Long menuId,
                                                @Param("roleId")Long roleId, @Param("userId")Long userId);

    /**
     * 获取API权限
     *
     * @param map
     * @return
     */
    List<AuthorityApi> selectAuthorityApi(@Param("status")Integer status, @Param("serviceId")String serviceId);
}
