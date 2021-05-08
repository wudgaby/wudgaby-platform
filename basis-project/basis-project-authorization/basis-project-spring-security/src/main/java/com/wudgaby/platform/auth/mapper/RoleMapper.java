package com.wudgaby.platform.auth.mapper;

import com.wudgaby.platform.auth.model.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author WudGaby
 * @since 2020-04-17
 */
public interface RoleMapper extends BaseMapper<Role> {
    Set<String> findByUrl(@Param("url") String url);
    Set<String> findByUrlAndMethod(@Param("url") String url, @Param("method") String method);
}
