package com.wudgaby.platform.sso.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wudgaby.platform.sso.server.entity.Resource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author jimi
 * @since 2020-11-19
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 获取子系统用户权限
     * @return
     */
    List<Resource> listResourceByUserId(@Param("userId") Long userId, @Param("sysCode") String sysCode);
}
