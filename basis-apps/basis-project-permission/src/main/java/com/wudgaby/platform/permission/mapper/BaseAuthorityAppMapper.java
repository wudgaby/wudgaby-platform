package com.wudgaby.platform.permission.mapper;

import com.wudgaby.platform.permission.dto.OpenAuthority;
import com.wudgaby.platform.permission.entity.BaseAuthorityApp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统权限-应用关联 Mapper 接口
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
public interface BaseAuthorityAppMapper extends BaseMapper<BaseAuthorityApp> {
    /**
     * 获取应用已授权权限
     *
     * @param appId
     * @return
     */
    List<OpenAuthority> selectAuthorityByApp(@Param("appId") String appId);
}
