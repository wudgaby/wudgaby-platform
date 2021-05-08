package com.wudgaby.oauth2.uaa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wudgaby.oauth2.uaa.entity.Role;
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
