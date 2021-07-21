package com.wudgaby.platform.permission.mapper;

import com.wudgaby.platform.permission.dto.AuthorityMenu;
import com.wudgaby.platform.permission.dto.OpenAuthority;
import com.wudgaby.platform.permission.entity.BaseAuthorityUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统权限-用户关联 Mapper 接口
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
public interface BaseAuthorityUserMapper extends BaseMapper<BaseAuthorityUser> {
    /**
     * 获取用户已授权权限
     *
     * @param userId
     * @return
     */
    List<OpenAuthority> selectAuthorityByUser(@Param("userId") Long userId);

    /**
     * 获取用户已授权权限完整信息
     *
     * @param userId
     * @return
     */
    List<AuthorityMenu> selectAuthorityMenuByUser(@Param("userId") Long userId);
}
