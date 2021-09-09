package com.wudgaby.oauth2.uaa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wudgaby.oauth2.uaa.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author WudGaby
 * @since 2020-04-17
 */
public interface UserMapper extends BaseMapper<User> {
    User findByAccount(@Param("account") String account);
    User findByUserName(@Param("username") String username);
    User findByPhone(@Param("phone") String phone);
    User findByEmail(@Param("email") String email);
}
