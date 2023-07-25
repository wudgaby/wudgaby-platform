package com.wudgaby.sample.data.sensitive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wudgaby.sample.data.sensitive.entity.SysUser;
import com.wudgaby.sample.data.sensitive.vo.UserForm;
import com.wudgaby.sample.data.sensitive.vo.UserRoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    List<UserRoleVo> listUserRoles(@Param("uid")Long uid);

    void addUserForm(@Param("uid")UserForm userForm);
}
