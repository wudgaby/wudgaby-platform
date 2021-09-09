package com.wudgaby.platform.sso.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wudgaby.platform.sso.server.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author jimi
 * @since 2020-11-19
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<String> listRoleListByUserId(@Param("userId") Long userId);
}
